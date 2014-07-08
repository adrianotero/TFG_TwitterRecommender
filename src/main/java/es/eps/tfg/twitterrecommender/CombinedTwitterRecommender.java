/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eps.tfg.twitterrecommender;

import java.util.List;
import net.recommenders.plista.client.Message;
import net.recommenders.plista.rec.combination.AbstractCombinationRecommender;
import net.recommenders.plista.recommender.RecentRecommender;
import net.recommenders.plista.recommender.Recommender;

/**
 *
 * @author ADRIAN
 */
public class CombinedTwitterRecommender extends AbstractCombinationRecommender implements Recommender {

    public CombinedTwitterRecommender() {
        super(new TwitterRecommender(), new RecentRecommender());
//        super(new TwitterRecommender(), new Recommender() {
//
//            @Override
//            public void init() {
//            }
//
//            @Override
//            public List<Long> recommend(Message _input, Integer limit) {
//                System.out.println("RandomRecommender.recommend");
//                List<Long> a = new ArrayList<Long>();
//                Random r = new Random();
//                for (int i = 0; i < limit; i++) {
//                    a.add(r.nextLong());
//                }
//                return a;
//            }
//
//            @Override
//            public void setProperties(Properties properties) {
//            }
//
//            @Override
//            public void impression(Message _impression) {
//            }
//
//            @Override
//            public void click(Message _click) {
//            }
//
//            @Override
//            public void update(Message _update) {
//            }
//        });
    }

    @Override
    public List<Long> recommend(Message input, Integer limit) {
        TwitterRecommender.tempLog.println("CTR:recommend_in\t" + input);
        List<Long> a = super.recommend(input, limit);
        TwitterRecommender.tempLog.println("CTR:recommend_out\t" + a);
        return a;
    }
}
