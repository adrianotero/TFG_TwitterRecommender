
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

    private static final String CONSUMER_KEY = ""; // To fill
    private static final String CONSUMER_SECRET = ""; // To fill
	private static final String OAUTH_ACCESS_TOKEN = ""; // To fill
	private static final String OAUTH_ACCESS_TOKEN_SECRET = ""; // To fill
    private static final boolean DEBUG = false;
    private Twitter twitter;

    public SearchTweets() {
        //Twitter Conf.
        System.setProperty("twitter4j.loggerFactory", "twitter4j.NullLoggerFactory");
        twitter4j.conf.ConfigurationBuilder cb = new twitter4j.conf.ConfigurationBuilder();
        cb.setDebugEnabled(DEBUG).setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET).setOAuthAccessToken(OAUTH_ACCESS_TOKEN).setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET);

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
