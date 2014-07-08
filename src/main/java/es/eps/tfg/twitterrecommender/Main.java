/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eps.tfg.twitterrecommender;

import java.util.Date;
import java.util.List;

/**
 *
 * @author ADRIAN
 */
public class Main {

    public static void main(String args[]) throws Exception {
		if ( args.length < 1) {
			System.out.println("Usage: fase(0=todo,1=ini BDs,2=actualizar BDs) [flag para borrar tablas]");
		}
		int fase = Integer.parseInt(args[0]);

        boolean drop = false;
		// Comprobar si se pasa un valor para variable drop
		if ( args.length > 1 ){
			drop = Boolean.parseBoolean(args[1]);
		}

        // Busca la base de datos, en caso de que no exista la crea.
        TweetDataBase tweetdb = TweetDataBase.GetInstance();

        // Comprueba si están creadas las tablas de la BD, si no existen son creadas
        if (tweetdb != null) {

			if ((fase == 0) || (fase == 1)) {
				// Si la opción drop esta activa todas las tablas de la BD son eliminadas al inicio
				if (drop) {
					System.out.println("");
					tweetdb.Drop_all_tables();
				}

				// Genera todas las tablas que no hayan sido creadas ya en la BD
				tweetdb.CreateTable_ALL();
				System.out.println("");

				// Busca los distintos dominios, querys y traducciones un fichero.
				ReadFromFile reader = new ReadFromFile();
				List<Domain> ListDomains = reader.ReadDomains("data/domains.txt");
				List<QueryStruct> ListQuerys = reader.ReadQuerys("data/querys.txt");
				List<Translation> ListTranslations = reader.ReadTranslations("data/translations.txt");

				// Inserta los dominios, querys y traducciones leidas en la BD
				for (Domain domain : ListDomains) {
					System.out.println("Insertando dominio " + domain.getDomainid() + "_" + domain.getDomain());
					tweetdb.Insert_Domains(domain.getDomainid(), domain.getDomain());
				}
				for (QueryStruct query : ListQuerys) {
					System.out.println("Insertando consulta " + query.getQueryId() + "_" + query.getQuery() + "_" + query.getDomainId());
					tweetdb.Insert_Querys(query.getQueryId(), query.getQuery(), query.getDomainId());
				}
				for (Translation translation : ListTranslations) {
					System.out.println("Insertando traduccion " + translation.getDomainId() + "_" + translation.getpListaId() + "_" + translation.getCategory());
					tweetdb.Insert_Code_Translations(translation.getDomainId(), translation.getpListaId(), translation.getCategory());
				}
			}

			if ((fase == 0) || (fase == 2)) {
				// Inicializa la clase encargada de la búsqueda mediante la API de Twitter
				SearchTweets buscador = new SearchTweets();
				List<TweetData> TweetList = null;

				// Actualiza el contenido de la BD mediante consultas a la API de Twitter cada X min
				while (true) {

					// Lee cada una de las querys desde la BD.
					List<QueryStruct> ListQuerys = tweetdb.SearchQuerys();

					// Busca los resultados para cada query de la BD en Twitter.
					for (QueryStruct query : ListQuerys) {
						System.out.println("Procesando consulta " + query.getQueryId() + " = " + query.getQuery());
						TweetList = null;
						TweetList = buscador.search(query);

						// Introducimos la fecha de actualizacion de los tweets.
						Date d = new Date();
						for (TweetData t : TweetList) {
							t.setLastUpdate(d);
						}

						// Introduce cada uno de los tweets obtenidos en la base de datos.
						tweetdb.Insert_Tweets(TweetList);

						// Duerme hasta la siguiente iteración (busqueda en twitter)
						// evitar rate limit
						int m = 1;
						int nSec = m * 60;
						System.out.println("Espera de " + nSec + "s");
						Thread.sleep(1000 * nSec);
					}

					// Actualiza el contenido de la tabla "Popularity_Recommender" a partir de los tweets almacenados
					tweetdb.RefreshPopularityRec();

					// Duerme hasta la siguiente iteración (busqueda en twitter)
					// evitar actualizaciones innecesarias
					int h = 1;
					int nSec = h * 60 * 60; // h horas
					System.out.println("Espera de " + nSec + "s");
					Thread.sleep(1000 * nSec);
				}
	        }
        }
    }
}
