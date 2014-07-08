/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eps.tfg.twitterrecommender;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;

/**
 *
 * @author ADRIAN
 */
public class TweetDataBase {

    private static TweetDataBase instance = null;
    private Connection connection = null;

    // Constructor
    protected TweetDataBase() {

        // Establece conexion con la base de datos, en caso de que no exista la crea.
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:data/twitterdb.db");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Patron singleton, devuelve una instancia de la clase si esta ha sido creada previamente.
    public static TweetDataBase GetInstance() {
        if (instance == null) {
            instance = new TweetDataBase();
        }
        return instance;
    }

    // Cierra la conexion con la base de datos.
    public void Close() {
        try {
            if (connection != null) {
                connection.close();
                instance = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // connection close failed.
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * *************************************************
     * Creacion de las tablas en la base de datos.
     * *************************************************
     */
    // En esta tabla se almacenarán los tweets obtenidos en cada busqueda.
    public void CreateTable_HISTORICAL() {
        String sql = "";

        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            sql = "CREATE TABLE IF NOT EXISTS HISTORICAL "
                    + "(TWEETID      LONG        PRIMARY KEY     NOT NULL, "
                    + " TWEETURL     CHAR(50)    NOT NULL, "
                    + " RETWEETS     INT         NOT NULL, "
                    + " FAVOURITES   INT         NOT NULL, "
                    + " CREATEAT     CHAR(50)    NOT NULL, "
                    + " TEXT         CHAR(140)   NOT NULL, "
                    + " USERID       LONG        NOT NULL, "
                    + " USERNAME     CHAR(50)    NOT NULL, "
                    + " QUERYID      LONG        NOT NULL, "
                    + " LASTUPDATE   CHAR(50)    NOT NULL, "
                    + " URLS         CHAR(140)   NOT NULL);";

            statement.executeUpdate(sql);
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // En esta tabla se almacenaran los distintos dominios
    public void CreateTable_DOMAIN() {
        String sql = "";

        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            sql = "CREATE TABLE IF NOT EXISTS DOMAIN "
                    + "(DOMAINID     LONG        PRIMARY KEY     NOT NULL, "
                    + " DOMAINTEXT   CHAR(50)    NOT NULL);";

            statement.executeUpdate(sql);
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // En esta tabla se almacenaran las distintas Querys
    public void CreateTable_QUERY() {
        String sql = "";

        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            sql = "CREATE TABLE IF NOT EXISTS QUERY "
                    + "(QUERYID      LONG        PRIMARY KEY     NOT NULL, "
                    + " QUERYTEXT    CHAR(50)    NOT NULL,"
                    + " DOMAINID     LONG        NOT NULL);";

            statement.executeUpdate(sql);
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // En esta tabla se almacenaran las urls mencionadas en un tweet junto con su "popularidad"
    public void CreateTable_POPULARITY_RECOMMENDER() {
        String sql = "";

        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            sql = "CREATE TABLE IF NOT EXISTS POPULARITY_RECOMMENDER "
                    + "(URL          CHAR(140)   NOT NULL, "
                    + " DOMAINID     LONG        NOT NULL, "
                    + " RETWEETS     INT         NOT NULL, "
                    + " FAVOURITES   INT         NOT NULL, "
                    + " PRIMARY KEY (URL, DOMAINID));";

            statement.executeUpdate(sql);
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // En esta tabla se almacenara toda la informacion obtenida de PLISTA.
    public void CreateTable_PLISTA() {
        String sql = "";

        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            sql = "CREATE TABLE IF NOT EXISTS PLISTA "
                    + "(ITEMID               LONG        PRIMARY KEY     NOT NULL, "
                    + " DOMAINID             LONG        NOT NULL, "
                    + " URL                  CHAR(140)   NOT NULL, "
                    + " CATEGORY             CHAR(50)    NOT NULL, "
                    + " FLAG_RECOMMENDABLE   INT         NOT NULL, "
                    + " TITTLE               CHAR(50), "
                    + " TEXT                 CHAR(140));";

            statement.executeUpdate(sql);
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // En esta tabla se almacenara toda la informacion obtenida de PLISTA.
    public void CreateTable_CODE_TRANSLATION() {
        String sql = "";

        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            sql = "CREATE TABLE IF NOT EXISTS CODE_TRANSLATION "
                    + "(DOMAINID             LONG    NOT NULL, "
                    + " PLISTAID             LONG    NOT NULL, "
                    + " CATEGORY             INT     NOT NULL, "
                    + " PRIMARY KEY (DOMAINID, PLISTAID, CATEGORY));";

            statement.executeUpdate(sql);
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Crea todas las tablas (si no existen) en la base de datos.
    public void CreateTable_ALL() {

        this.CreateTable_HISTORICAL();
        this.CreateTable_DOMAIN();
        this.CreateTable_QUERY();
        this.CreateTable_POPULARITY_RECOMMENDER();
        this.CreateTable_PLISTA();
        this.CreateTable_CODE_TRANSLATION();
    }

    /**
     * ***********************************************************
     * Insercion/Actualizacion en las tablas en la base de datos.
     * ***********************************************************
     */
    // Inserta nuevos tweets en la tabla HISTORICAL de la base de datos.
    public void Insert_Tweets(List<TweetData> TweetList) {
        String sql = null;
        TweetData tweet_actual = null;

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            for (TweetData tweet : TweetList) {

                tweet_actual = tweet;
                sql = "INSERT INTO HISTORICAL (TWEETID,TWEETURL,RETWEETS,FAVOURITES,CREATEAT,"
                        + "TEXT,USERID,USERNAME,QUERYID,LASTUPDATE,URLS) VALUES ("
                        + tweet.getId() + ", "
                        + "'" + tweet.getTweeturl() + "', "
                        + tweet.getRetweets() + ", "
                        + tweet.getFavoritos() + ", "
                        + "'" + tweet.getCreateat() + "', "
                        + "'" + tweet.getTexto() + "', "
                        + tweet.getUserid() + ", "
                        + "'" + tweet.getUsername() + "', "
                        + tweet.getQueryId() + ", "
                        + "'" + tweet.getLastUpdate() + "', "
                        + "'" + tweet.getUrls() + "');";
                statement.executeUpdate(sql);
            }

            statement.close();
            connection.commit();

        } catch (Exception e) {

            // Comprueba si se trata de un fallo de violacion de clave primaria (En ese caso actualizar registro)
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                this.Update_Tweet(tweet_actual);
            } else {
                e.printStackTrace();
                System.err.println(sql);
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    // Actualiza un registro de la tabla HISTORICAL.
    public void Update_Tweet(TweetData tweet) {
        String sql = null;
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            sql = "UPDATE HISTORICAL SET "
                    + "TWEETURL = " + "'" + tweet.getTweeturl() + "',"
                    + "RETWEETS = " + tweet.getRetweets() + ","
                    + "FAVOURITES = " + tweet.getFavoritos() + ","
                    + "CREATEAT = " + "'" + tweet.getCreateat() + "',"
                    + "TEXT = " + "'" + tweet.getTexto() + "',"
                    + "USERID = " + tweet.getUserid() + ","
                    + "USERNAME = " + "'" + tweet.getUsername() + "',"
                    + "QUERYID = " + tweet.getQueryId() + ","
                    + "LASTUPDATE = " + "'" + tweet.getLastUpdate() + "',"
                    + "URLS = " + "'" + tweet.getUrls() + "' "
                    + "WHERE TWEETID = " + tweet.getId() + ";";

            statement.executeUpdate(sql);

            statement.close();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Inserta un nuevo Dominio en la base de datos.
    public void Insert_Domains(long domainid, String domain) {

        String sql = null;

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            sql = "INSERT INTO DOMAIN (DOMAINID,DOMAINTEXT) VALUES ("
                    + domainid + ", "
                    + "'" + domain + "');";

            statement.executeUpdate(sql);

            statement.close();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Inserta un nuevo Dominio en la base de datos.
    public void Insert_Querys(long queryid, String query, long domainid) {
        String sql = null;

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            sql = "INSERT INTO QUERY (QUERYID,QUERYTEXT,DOMAINID) VALUES ("
                    + queryid + ", "
                    + "'" + query + "', "
                    + domainid + ");";

            statement.executeUpdate(sql);

            statement.close();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Inserta un elemento en la tabla POPULARITY_RECOMMENDER
    public void InsertIntoPopularityRec(String url, long domainid, int favourites, int retweets) {
        String query = null;

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.


            query = ("INSERT INTO POPULARITY_RECOMMENDER values ("
                    + "'" + url + "',"
                    + domainid + ", "
                    + retweets + ", "
                    + favourites + ");");

            statement.executeUpdate(query);

            statement.close();
            connection.commit();

        } catch (Exception e) {

            // Comprueba si se trata de un fallo de violacion de clave primaria (En ese caso actualizar registro)
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                this.UpdatePopularityRec(url, domainid, retweets, favourites);
            } else {
                e.printStackTrace();
                System.err.println(query);
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    public void UpdatePopularityRec(String url, long domainid, int retweets, int favourites) {

        String query = null;

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            query = ("UPDATE POPULARITY_RECOMMENDER SET "
                    + "DOMAINID = " + domainid + ", "
                    + "RETWEETS = " + retweets + ", "
                    + "FAVOURITES = " + favourites + ", "
                    + "WHERE URL = " + url + ");");

            statement.executeUpdate(query);

            statement.close();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(query);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        }
    }

    public void RefreshPopularityRec() {
        String query = null;

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            query = "DROP TABLE IF EXISTS TEMP;";
            statement.executeUpdate(query);
            query = "DROP TABLE IF EXISTS TEMP2;";
            statement.executeUpdate(query);

            query = "CREATE TABLE IF NOT EXISTS TEMP "
                    + "(URL          CHAR(140)   NOT NULL, "
                    + " DOMAINID     LONG        NOT NULL, "
                    + " RETWEETS     INT         NOT NULL, "
                    + " FAVOURITES   INT         NOT NULL)";
            statement.executeUpdate(query);

            List<TweetData> TweetList_read = this.Select_Tweets("");
            for (TweetData tweet : TweetList_read) {

                //QueryStruct q = this.Search_by_QueryId(tweet.getQueryId());
                //QueryStruct q = new QueryStruct(1l,"asd",1l);

                String[] urls = tweet.getUrls().split("\n");
                for (String url : urls) {

                    if (!url.equals("")) {
                        // Tratar por separado cada url del tweet
                        query = ("INSERT INTO TEMP VALUES ("
                                + "'" + url + "', "
                                + tweet.getDomainid() + ", "
                                + tweet.getRetweets() + ", "
                                + tweet.getFavoritos() + ");");

                        statement.executeUpdate(query);
                    }
                }
            }
            // Agrupar en función de la URL
            query = "CREATE TABLE IF NOT EXISTS TEMP2 AS "
                    + "SELECT URL, DOMAINID, SUM(RETWEETS) AS TOT_RETWEETS, SUM(FAVOURITES) AS TOT_FAVOURITES "
                    + "FROM TEMP "
                    + "GROUP BY URL, DOMAINID "
                    + "ORDER BY (TOT_RETWEETS + TOT_FAVOURITES) DESC;";

            statement.executeUpdate(query);

            // Insertar los nuevos valores calculados en la BD
            this.Clean("POPULARITY_RECOMMENDER");
            query = "INSERT INTO POPULARITY_RECOMMENDER SELECT URL, DOMAINID, TOT_RETWEETS, TOT_FAVOURITES FROM TEMP2;";
            statement.executeUpdate(query);

            statement.close();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(query);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Inserta nuevos tweets en la tabla PLista de la base de datos.
    public void Insert_PLista(PLista plista) {
        String sql = null;

        try {
            sql = "INSERT INTO PLISTA (ITEMID,DOMAINID,URL,CATEGORY,"
                    + "FLAG_RECOMMENDABLE,TEXT,TITTLE) VALUES ("
                    + plista.getItemId() + ", "
                    + plista.getDomainId() + ", "
                    + "'" + plista.getUrl() + "', "
                    + "'" + plista.getCategory() + "', "
                    + plista.getFlag_Recommendable() + ", "
                    + "'" + plista.getText() + "', "
                    + "'" + plista.getTittle() + "');";
        } catch (Exception e) {
            // algún campo de plista no es válido
            return;
        }

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate(sql);

            statement.close();
            connection.commit();

        } catch (Exception e) {
            // ejecutar update por defecto
            sql = "UPDATE PLISTA SET "
                    + "ITEMID = " + plista.getItemId() + ", "
                    + "DOMAINID = " + plista.getDomainId() + ", "
                    + "URL = '" + plista.getUrl() + "', "
                    + "CATEGORY = '" + plista.getCategory() + "', "
                    + "FLAG_RECOMMENDABLE = " + plista.getFlag_Recommendable() + ", "
                    + "TEXT = '" + plista.getText() + "', "
                    + "TITTLE = '" + plista.getTittle() + "' "
                    + "WHERE ITEMID = " + plista.getItemId() + ";";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);

                statement.close();
                connection.commit();
            } catch (Exception e2) {
                e2.printStackTrace();
                // Comprueba si se trata de un fallo de violacion de clave primaria (En ese caso actualizar registro)
                //if(e.getMessage().contains("UNIQUE constraint failed")){
                //this.Update();
                //} else {
                System.err.println(sql);
                System.err.println(e2.getClass().getName() + ": " + e2.getMessage());
                //}
            }
        }
    }

    // Inserta nuevos tweets en la tabla PLista de la base de datos.
    public void Insert_Code_Translations(long domainid, long plistaid, String category) {
        String sql = null;

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            sql = "INSERT INTO CODE_TRANSLATION (DOMAINID,PLISTAID,CATEGORY) VALUES ("
                    + domainid + ", "
                    + plistaid + ", "
                    + "'" + category + "');";
            statement.executeUpdate(sql);

            statement.close();
            connection.commit();

        } catch (Exception e) {

            e.printStackTrace();
            // Comprueba si se trata de un fallo de violacion de clave primaria (En ese caso actualizar registro)
            //if(e.getMessage().contains("UNIQUE constraint failed")){
            //this.Update();
            //} else {
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //}
        }
    }

    /**
     * *************************************************
     * Consultas contra las tablas de la base de datos.
     * *************************************************
     */
    // Devuelve una lista con los tweets que cumple unas determinadas condiciones.
    public List<TweetData> Select_Tweets(String where) {
        try {
            List<TweetData> TweetList = new ArrayList<TweetData>();
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            // Usado para generar fechas a partir de un string.
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);

            Date createat = null;
            Date lastupdate = null;
            TweetData tweet = null;
            ResultSet results = statement.executeQuery("SELECT A.*, B.QUERYTEXT, B.DOMAINID FROM HISTORICAL AS A LEFT JOIN QUERY AS B ON B.QUERYID = A.QUERYID" + where + ";");
            while (results.next()) {

                createat = formatter.parse(results.getString("CREATEAT"));
                lastupdate = formatter.parse(results.getString("LASTUPDATE"));

                tweet = new TweetData(
                        results.getLong("TWEETID"),
                        results.getString("TWEETURL"),
                        results.getInt("RETWEETS"),
                        results.getInt("FAVOURITES"),
                        createat,
                        results.getString("TEXT"),
                        results.getLong("USERID"),
                        results.getString("USERNAME"),
                        results.getLong("QUERYID"),
                        lastupdate,
                        results.getString("URLS"));

                tweet.setDomainid(results.getLong("DOMAINID"));

                TweetList.add(tweet);
            }

            results.close();
            statement.close();
            connection.commit();
            return TweetList;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    // Busca una determinada query a partir de su "QueryId"
    public QueryStruct Search_by_QueryId(long id) {
        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            String query = "";
            long QueryId = 0l;
            QueryStruct q = null;
            long DomainId = 0l;

            ResultSet results = statement.executeQuery("SELECT * FROM QUERY WHERE QUERYID = " + id + ";");
            while (results.next()) {

                QueryId = results.getLong("QUERYID");
                query = results.getString("QUERYTEXT");
                DomainId = results.getLong("DOMAINID");

                q = new QueryStruct(QueryId, query, DomainId);
            }

            results.close();
            statement.close();
            connection.commit();
            return q;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    // Busca una determinada queryid a partir de una "Query"
    public long Search_by_Query(String query) {
        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            long queryid = -1l;
            ResultSet results = statement.executeQuery("SELECT * FROM QUERY WHERE QUERYTEXT = '" + query + "';");
            while (results.next()) {

                queryid = results.getLong("QUERYID");
            }

            results.close();
            statement.close();
            connection.commit();
            return queryid;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return -1l;
        }
    }

    // Busca todas las "querys" almacenadas en la BD
    public List<QueryStruct> SearchQuerys() {
        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            List<QueryStruct> QueryList = new ArrayList<QueryStruct>();
            String query = "";
            long QueryId = 0l;
            QueryStruct q = null;
            long DomainId = 0l;

            ResultSet results = statement.executeQuery("SELECT * FROM QUERY;");
            while (results.next()) {

                QueryId = results.getLong("QUERYID");
                query = results.getString("QUERYTEXT");
                DomainId = results.getLong("DOMAINID");

                q = new QueryStruct(QueryId, query, DomainId);

                QueryList.add(q);
            }

            results.close();
            statement.close();
            connection.commit();
            return QueryList;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * ***************************************************************************
     * Eliminacion de las tablas (unicamente de su contenido) en la base de
     * datos.
     * ***************************************************************************
     */
    // Borra el contenido de una tabla de la base de datos.
    public void Clean(String tabla) {
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            String sql = "DELETE FROM " + tabla + ";";
            statement.executeUpdate(sql);

            statement.close();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Elimina todas las tablas de la base de datos.
    public void Drop_all_tables() {
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            String sql = "DROP TABLE if exists domain;";
            statement.executeUpdate(sql);
            sql = "DROP TABLE if exists query;";
            statement.executeUpdate(sql);
            sql = "DROP TABLE if exists historical;";
            statement.executeUpdate(sql);
            sql = "DROP TABLE if exists popularity_recommender;";
            statement.executeUpdate(sql);
            sql = "DROP TABLE if exists plista;";
            statement.executeUpdate(sql);
            sql = "DROP TABLE if exists code_translation;";
            statement.executeUpdate(sql);
            sql = "DROP TABLE if exists temp;";
            statement.executeUpdate(sql);
            sql = "DROP TABLE if exists temp2;";
            statement.executeUpdate(sql);

            statement.close();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Envia una consulta contra la base de datos.
    public void executeUpdate(String query) {
        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate(query);

            statement.close();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        }
    }

    List<String> getUrlsOrderedByPopularity(long domain, int favWeight, int retWeight) {

        List<String> urls = new ArrayList<String>();

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet results = statement.executeQuery("SELECT * FROM POPULARITY_RECOMMENDER"
                    + " WHERE DOMAINID = " + domain
                    + " ORDER BY (" + favWeight + "* FAVOURITES + "
                    + retWeight + "*RETWEETS) DESC;");

            while (results.next()) {

                urls.add(results.getString("URL"));
            }

            results.close();

            statement.close();
            connection.commit();

            return urls;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return urls;
        }
    }

    PLista searchPListaURL(String url) {

        PLista item = null;

        // Add case where url is at the end:
            /*
         * http://news.google.com/news/url?sa=t&fd=R&ct2=de&usg=AFQjCNG4yCmpMy8v7rPwPAZu0Mf3eLjDnA&clid=c3a7d30bb8a4878e06b80cf16b898331&cid=52778917544762&ei=XW6lU4CmHYHnkAWa04DwAw
         * &url=http://www.tagesspiegel.de/kultur/zum-70-geburtstag-des-filmregisseurs-helmut-di
         */
        String parsedUrl = url;
        if (url.contains("=")) {
            parsedUrl = url.substring(url.lastIndexOf("="));
        }

        String query = "SELECT * FROM PLISTA WHERE URL = \"" + url + "\" OR URL = \"" + parsedUrl + "\";";

        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.


//            ResultSet results = statement.executeQuery("SELECT * FROM PLISTA WHERE URL = '" + url + "';");
//            ResultSet results = statement.executeQuery("SELECT * FROM PLISTA WHERE URL = '" + url + "' OR URL = '" + parsedUrl + "';");
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                item = new PLista(results.getLong("ITEMID"),
                        results.getLong("DOMAINID"),
                        results.getString("URL"),
                        results.getString("CATEGORY"),
                        results.getInt("FLAG_RECOMMENDABLE"),
                        results.getString("TITTLE"),
                        results.getString("TEXT"));
            }

            results.close();

            statement.close();
            connection.commit();

            return item;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(query);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    long translatePListaDomain(long domain) {
        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            long domainId = -1l;

            ResultSet results = statement.executeQuery("SELECT * FROM CODE_TRANSLATION WHERE PLISTAID = " + domain + ";");
            while (results.next()) {

                domainId = results.getLong("DOMAINID");
            }
            results.close();

            statement.close();
            connection.commit();

            return domainId;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return -1L;
        }
    }
}
