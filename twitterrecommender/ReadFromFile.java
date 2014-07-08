/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eps.tfg.twitterrecommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author ADRIAN
 */
public class ReadFromFile {

    private File file;
    private FileReader fr;
    private BufferedReader br;

    // Constructor.
    public ReadFromFile() {
    }

    // Lee una lista de Querys desde un fichero de texto.
    public List<QueryStruct> ReadQuerys(String ruta) {
        try {
            List<QueryStruct> ListQuerys = new ArrayList<QueryStruct>();

            // Se busca el fichero a leer.
            file = new File(ruta);
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            // Se obtienen los datos desde el fichero.
            long queryid = 0l;
            String query = "";
            long domainid = 0l;
            String linea;
            while ((linea = br.readLine()) != null) {

                StringTokenizer tokens = new StringTokenizer(linea, ",");

                if (tokens.hasMoreTokens()) {
                    queryid = Long.valueOf(tokens.nextToken());

                    if (tokens.hasMoreTokens()) {
                        query = tokens.nextToken();

                        if (tokens.hasMoreTokens()) {
                            domainid = Long.valueOf(tokens.nextToken());
                            ListQuerys.add(new QueryStruct(queryid, query, domainid));
                        }
                    }
                }
            }
            
            return ListQuerys;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        } finally {
            // Asegura el cierre del fichero en cualquiera de los casos.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                System.err.println(e2.getClass().getName() + ": " + e2.getMessage());
                return null;
            }
        }
    }

    public List<Domain> ReadDomains(String ruta) {
        try {
            List<Domain> ListDomains = new ArrayList<Domain>();

            // Se busca el fichero a leer.
            file = new File(ruta);
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            // Se obtienen los datos desde el fichero.
            long domainid = 0l;
            String domain = "";
            String linea;
            while ((linea = br.readLine()) != null) {

                StringTokenizer tokens = new StringTokenizer(linea, ",");

                if (tokens.hasMoreTokens()) {
                    domainid = Long.valueOf(tokens.nextToken());

                    if (tokens.hasMoreTokens()) {
                        domain = tokens.nextToken();

                        ListDomains.add(new Domain(domainid, domain));
                    }
                }
            }

            return ListDomains;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        } finally {
            // Asegura el cierre del fichero en cualquiera de los casos.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                System.err.println(e2.getClass().getName() + ": " + e2.getMessage());
                return null;
            }
        }
    }

    public List<Translation> ReadTranslations(String ruta) {
        try {
            List<Translation> ListTranslations = new ArrayList<Translation>();

            // Se busca el fichero a leer.
            file = new File(ruta);
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            // Se obtienen los datos desde el fichero.
            long domainid = 0l;
            long plistaid = 0l;
            String category = "";
            String linea;
            while ((linea = br.readLine()) != null) {

                StringTokenizer tokens = new StringTokenizer(linea, ",");

                if (tokens.hasMoreTokens()) {
                    domainid = Long.valueOf(tokens.nextToken());

                    if (tokens.hasMoreTokens()) {
                        plistaid = Long.valueOf(tokens.nextToken());

                        if (tokens.hasMoreTokens()) {
                            category = tokens.nextToken();

                            ListTranslations.add(new Translation(-1l,domainid,plistaid,category));
                        }
                    }
                }
            }

            return ListTranslations;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        } finally {
            // Asegura el cierre del fichero en cualquiera de los casos.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                System.err.println(e2.getClass().getName() + ": " + e2.getMessage());
                return null;
            }
        }
    }
}

