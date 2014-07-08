/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eps.tfg.twitterrecommender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ADRIAN
 */
public class Main_Test {

    public static void main(String args[]) throws Exception {
        // Busca la base de datos, en caso de que no exista la crea.
        TweetDataBase tweetdb = TweetDataBase.GetInstance();

        System.out.println("Escoge una opcion: ");
        System.out.println("0) Inicializar la base de datos.");
        System.out.println("1) Escribir en la base de datos.");

        System.out.println("2) Insercion de datos en PLista.");
        System.out.println("3) Interaccion con PLista.");

        System.out.println("4) Leer desde la base de datos.");
        System.out.println("5) Borrar el contenido de la base de datos.");

        System.out.println("6) EXTRA.");


        BufferedReader lectura = new BufferedReader(new InputStreamReader(System.in));
        int opcion;
//        opcion = Integer.parseInt(lectura.readLine());
        opcion = 1;
        switch (opcion) {

            case 0:
                // Comprueba si están creadas las tablas de la BD
                if (tweetdb != null) {

                    // Genera todas las tablas que no hayan sido creadas ya en la BD
                    tweetdb.CreateTable_ALL();
                }

                break;


            case 1: {
                // Busca las distintas querys de un fichero.
                ReadFromFile reader = new ReadFromFile();
                List<Domain> ListDomains = reader.ReadDomains("data/domains.txt");
                List<QueryStruct> ListQuerys = reader.ReadQuerys("data/querys.txt");
                List<Translation> ListTranslations = reader.ReadTranslations("data/translations.txt");

                // Introduce las querys y dominios leidos en la base de datos.
                if (tweetdb != null) {
                    for (Domain domain : ListDomains) {
                        tweetdb.Insert_Domains(domain.getDomainid(), domain.getDomain());
                    }
                    for (QueryStruct query : ListQuerys) {
                        tweetdb.Insert_Querys(query.getQueryId(), query.getQuery(), query.getDomainId());

                        // Busca los resultados para cada query en Twitter.
                        SearchTweets buscador = new SearchTweets();
                        List<TweetData> TweetList = buscador.search(query);

                        // Actualizamos la fecha de actualizacion de los tweets.
                        Date d = new Date();
                        for (TweetData t : TweetList) {
                            t.setLastUpdate(d);
                        }

                        // Introduce cada uno de los tweets obtenidos en la base de datos.
                        tweetdb.Insert_Tweets(TweetList);
                    }
                    for (Translation translation : ListTranslations) {
                        tweetdb.Insert_Code_Translations(translation.getDomainId(), translation.getpListaId(), translation.getCategory());
                    }

                    Thread.sleep(1000 * 2);
                }
            }
            break;

            case 2:
                PLista plista = null;

                plista = new PLista(1l, 1l, "http://as.com/futbol/2014/05/19/primera/1400523393_991035.html", "Categoria_1", 1, "NOTICIA_AS_2", "TEXTO_2");
                tweetdb.Insert_PLista(plista);

                plista = new PLista(2l, 2l, "http://www.marca.com/2014/05/18/futbol/futbol_internacional/1400443261.html", "Categoria_1", 1, "NOTICIA_MARCA_2", "TEXTO_4");
                tweetdb.Insert_PLista(plista);

                plista = new PLista(3l, 2l, "http://www.marca.com/2014/05/19/futbol/equipos/barcelona/1400523529.html?a=3d64e5626fae8df4411e706e7c764958&t=1400523817", "Categoria_1", 1, "NOTICIA_MARCA_1", "TEXTO_3");
                tweetdb.Insert_PLista(plista);

                plista = new PLista(4l, 1l, "http://as.com/futbol/2014/05/19/champions/1400515294_671294.html", "Categoria_1", 1, "NOTICIA_AS_1", "TEXTO_1");
                tweetdb.Insert_PLista(plista);

                plista = new PLista(5l, 2l, "http://www.marca.com/2014/05/27/futbol/seleccion/1401141718.html", "Categoria_1", 1, "NOTICIA_MARCA_3", "TEXTO_1");
                tweetdb.Insert_PLista(plista);

                plista = new PLista(6l, 2l, "http://www.marca.com/2014/05/26/futbol/equipos/real_madrid/1401101095.html?cid=SMBOSO34503&s_kw=Twitter", "Categoria_1", 1, "NOTICIA_MARCA_4", "TEXTO_1");
                tweetdb.Insert_PLista(plista);

                break;

            case 3:
                // Actualiza el contenido de la tabla "Popularity_Recommender" a partir de los tweets almacenados
                tweetdb.RefreshPopularityRec();
                break;

            case 4:

                // Lee los tweets de la base de datos.
                if (tweetdb != null) {
                    List<TweetData> TweetList_read = tweetdb.Select_Tweets("");
                    for (TweetData tweet : TweetList_read) {
                        System.out.println("ID = " + tweet.getId());
                        System.out.println("TWEETURL = " + tweet.getTweeturl());
                        System.out.println("RETWEETS = " + tweet.getRetweets());
                        System.out.println("FAVOURITES = " + tweet.getFavoritos());
                        System.out.println("CREATE AT = " + tweet.getCreateat());
                        System.out.println("TEXT = " + tweet.getTexto());
                        System.out.println("USERID = " + tweet.getUserid());
                        System.out.println("USERNAME = " + tweet.getUsername());
                        System.out.println("QUERYID = " + tweet.getQueryId());
                        System.out.println("LASTUPDATE = " + tweet.getLastUpdate());
                        System.out.println("URLS = " + tweet.getUrls());
                        System.out.println("---------------------------------------------");
                        System.out.println();
                    }
                }
                break;

            case 5:
                if (tweetdb != null) {
                    tweetdb.Clean("HISTORICAL");
                    tweetdb.Clean("DOMAIN");
                    tweetdb.Clean("QUERY");
                    tweetdb.Clean("POPULARITY_RECOMMENDER");
                    tweetdb.Clean("PLISTA");
                    tweetdb.Clean("CODE_TRANSLATION");
                }
                break;

            case 6:
                tweetdb.CreateTable_CODE_TRANSLATION();
                tweetdb.Insert_Code_Translations(1, 2, "domain");
                tweetdb.Insert_Code_Translations(5, 1, "domain");
                break;

            case 7: {
                // Busca las distintas querys de un fichero.
                ReadFromFile reader = new ReadFromFile();
                List<Domain> ListDomains = reader.ReadDomains("Data/domains.txt");
                List<QueryStruct> ListQuerys = reader.ReadQuerys("Data/querys.txt");
                List<Translation> ListTranslations = reader.ReadTranslations("Data/translations.txt");

                // Introduce las querys y dominios leidos en la base de datos.
                if (tweetdb != null) {

                    // Genera todas las tablas que no hayan sido creadas ya en la BD
                    tweetdb.CreateTable_ALL();

//                    while (true) {
                    for (Domain domain : ListDomains) {
                        tweetdb.Insert_Domains(domain.getDomainid(), domain.getDomain());
                    }
                    for (QueryStruct query : ListQuerys) {
                        tweetdb.Insert_Querys(query.getQueryId(), query.getQuery(), query.getDomainId());

                        // Busca los resultados para cada query en Twitter.
                        SearchTweets buscador = new SearchTweets();
                        List<TweetData> TweetList = buscador.search(query);

                        // Actualizamos la fecha de actualizacion de los tweets.
                        Date d = new Date();
                        for (TweetData t : TweetList) {
                            t.setLastUpdate(d);
                        }

                        // Introduce cada uno de los tweets obtenidos en la base de datos.
                        tweetdb.Insert_Tweets(TweetList);
                    }
                    for (Translation translation : ListTranslations) {
                        tweetdb.Insert_Code_Translations(translation.getDomainId(), translation.getpListaId(), translation.getCategory());
                    }
                    // Actualiza el contenido de la tabla "Popularity_Recommender" a partir de los tweets almacenados
                    tweetdb.RefreshPopularityRec();
                    /*
                    int h = 1;
                    int nSec = h * 60 * 60; // h horas
                    Thread.sleep(1000 * nSec);
                     *///                    }
                }
            }
            break;
        }

        // Cierra la base de datos.
        tweetdb.Close();
    }
}
