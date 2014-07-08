
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eps.tfg.twitterrecommender;

import java.util.ArrayList;
import twitter4j.*;
import java.util.List;

/**
 *
 * @author ADRIAN
 */
public class SearchTweets {

    private static final boolean DEBUG = false;
    private Twitter twitter;

    public SearchTweets() {
        //Twitter Conf.
        System.setProperty("twitter4j.loggerFactory", "twitter4j.NullLoggerFactory");
        twitter4j.conf.ConfigurationBuilder cb = new twitter4j.conf.ConfigurationBuilder();
//        cb.setDebugEnabled(DEBUG);
//        cb.setDebugEnabled(DEBUG).setOAuthConsumerKey("mXaVIYhwYzs0c7lkRniPrw");
//        cb.setDebugEnabled(DEBUG).setOAuthConsumerSecret("nd1xBXCtr7jvz7pNpKWlmqtZsHRzsBXLNkvUXlZGNNE");
//        cb.setDebugEnabled(DEBUG).setOAuthAccessToken("2341141538-xa9PPHdK7Rb7CjKd0GowuNxBUCdeT4so1G6EZzr");
//        cb.setDebugEnabled(DEBUG).setOAuthAccessTokenSecret("jnB2GjJeAfEABGiqzdzMV1FXcqf1UK5h1gwyURKIzesrt");
        cb.setDebugEnabled(DEBUG).setOAuthConsumerKey("mXaVIYhwYzs0c7lkRniPrw").setOAuthConsumerSecret("nd1xBXCtr7jvz7pNpKWlmqtZsHRzsBXLNkvUXlZGNNE").setOAuthAccessToken("2341141538-xa9PPHdK7Rb7CjKd0GowuNxBUCdeT4so1G6EZzr").setOAuthAccessTokenSecret("jnB2GjJeAfEABGiqzdzMV1FXcqf1UK5h1gwyURKIzesrt");

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public List<TweetData> search(QueryStruct query) {

        List<TweetData> TweetList = null;

        try {
            Query q;
            QueryResult result;

            q = new Query(query.getQuery());

            do {
                q.count(500);
                result = twitter.search(q);
                List<Status> tweets = result.getTweets();

                TweetList = new ArrayList<TweetData>();

                for (Status tweet : tweets) {

                    String urls = "";
                    URLEntity[] urlEnt = tweet.getURLEntities();
                    for (URLEntity url : urlEnt) {
                        urls = urls + url.getExpandedURL() + "\n";
                    }

                    if (!tweet.isRetweet()) {

                        String tweeturl = "http://twitter.com/"
                                + tweet.getUser().getScreenName()
                                + "/status/" + tweet.getId();

                        TweetData TD = new TweetData(tweet.getId(),
                                tweeturl,
                                tweet.getRetweetCount(),
                                tweet.getFavoriteCount(),
                                tweet.getCreatedAt(),
                                tweet.getText().replace("'", "").replace("\"", ""),
                                tweet.getUser().getId(),
                                tweet.getUser().getScreenName(),
                                query.getQueryId(),
                                null,
                                urls);
                        TweetList.add(TD);
                    }
                }
//                return TweetList;

                int nSec = 10;
                Thread.sleep(1000 * nSec);

            } while ((q = result.nextQuery()) != null);
            return TweetList;

        } catch (TwitterException te) {

            System.err.println("Failed to search tweets: " + te.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
