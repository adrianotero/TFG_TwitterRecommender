/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eps.tfg.twitterrecommender;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import net.recommenders.plista.client.ChallengeMessage;
import net.recommenders.plista.client.Message;
import net.recommenders.plista.recommender.Recommender;

/**
 *
 * @author ADRIAN
 */
public class TwitterRecommender implements Recommender {

    private static final int MAX_REC_REQ_TO_CACHE = 100;
    static PrintStream tempLog = null;

    static {

        try {
            tempLog = new PrintStream(new FileOutputStream("/export/scratch2/recommend/tfg/tempLog.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private TweetDataBase tweetdb;
    private int favWeight;
    private int retWeight;
    private Map<Long, List<Long>> cacheDomainRecs;
    private int recommendationRequests;

    public TwitterRecommender() {
        // Busca la base de datos, en caso de que no exista la crea.
        tweetdb = TweetDataBase.GetInstance();

        cacheDomainRecs = new ConcurrentHashMap<Long, List<Long>>();
        recommendationRequests = 0;
    }

    public static void main(String[] args) {

        long domainID = 1677;

        ChallengeMessage result = new ChallengeMessage();
        result.setDomainID(domainID);

//        TwitterRecommender tr = new TwitterRecommender();
//        Recommender tr = new TwitterRecommender();
        Recommender tr = new CombinedTwitterRecommender();
        tr.setProperties(new Properties() {

            {
                put("twitter.weight.fav", "1");
                put("twitter.weight.rt", "1");
            }
        });

        System.out.println(tr.recommend(result, 4));
    }

    @Override
    public void init() {
    }

    @Override
    public List<Long> recommend(Message _input, Integer limit) {
//        System.out.println("TwitterRecommender.recommend");
        tempLog.println("recommend_in\t" + _input);

        final Long domain = _input.getDomainID();
        if (domain == null) {
            return new ArrayList<Long>(0);
        }
        List<Long> recommendations = cacheDomainRecs.get(domain);

//        getRecommendationsUsingDB(domain, recommendations, limit);
        if ((recommendations == null) || (recommendationRequests >= MAX_REC_REQ_TO_CACHE)) {
            final int maxLimit = 10 * limit;
            new Thread() {

                @Override
                public void run() {
                    List<Long> recommendations = new ArrayList<Long>();
                    getRecommendationsUsingDB(domain, recommendations, maxLimit);
                    cacheDomainRecs.put(domain, recommendations);
                    recommendationRequests = 0;
                }
            }.start();
        } else {
            recommendationRequests++;
        }

        if (recommendations.size() > limit) {
            recommendations = recommendations.subList(0, limit);
        }

        tempLog.println("recommend_out\t" + recommendations);
        return recommendations;
    }

    private void getRecommendationsUsingDB(Long domain, List<Long> recommendations, Integer limit) {
        Long true_domain = -1l;
        List<String> urls = null;
        int count = 0;
        PLista item = null;

        if (tweetdb != null) {

            // Buscar a partir del domainid dado por PLista el respectivo en el recomendador
            true_domain = tweetdb.translatePListaDomain(domain);

//            System.out.println(domain + "_" + true_domain);

            // Preguntar al recomendador por las urls para ese dominio
            urls = tweetdb.getUrlsOrderedByPopularity(true_domain, favWeight, retWeight);

            for (String url : urls) {
//                System.out.println("url:" + url);
                if (count < limit) {

                    // obtener el id correspondiente (tabla plista)
                    item = tweetdb.searchPListaURL(url);
                    if (item != null) {
                        // y comprobar flag (es recomendable?)
                        if (item.getFlag_Recommendable() == 1) {
                            recommendations.add(item.getItemId());
                            count++;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setProperties(Properties properties) {
        favWeight = Integer.parseInt(properties.getProperty("twitter.weight.fav", "2"));
        System.out.println("favWeight=" + favWeight);
        retWeight = Integer.parseInt(properties.getProperty("twitter.weight.rt", "2"));
        System.out.println("retWeight=" + retWeight);
    }

    @Override
    public void impression(Message _impression) {
        tempLog.println("impression_in\t" + _impression);
    }

    @Override
    public void click(Message _click) {
        tempLog.println("click_in\t" + _click);
    }

    @Override
    public void update(Message _update) {
        tempLog.println("update_in\t" + _update);
        // actualizar la tabla plista
        try {
            Long domain = _update.getDomainID();
            Long item = _update.getItemID();
            Boolean flag = _update.getItemRecommendable();
            Long category = _update.getItemCategory();
            String text = _update.getItemText();
            String title = _update.getItemTitle();
            String url = _update.getItemURL();

            PLista plista = new PLista(item, domain, url, category + "", flag ? 1 : 0, title, text);
            tweetdb.Insert_PLista(plista);

            tempLog.println("update_out\t" + "success");
        } catch (Exception e) {
//            System.err.println("Update en TwitterRecommender failed:");
//            e.printStackTrace();
        }
    }
}
