package magicstudio;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.AveragingPreferenceInferrer;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;

import java.util.List;
import java.io.File;

public class DriveMahout {
    public static void main(String[] args) throws Exception {
        DataModel model = new FileDataModel(new File("grouplensratings.csv"));
        UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
        userSimilarity.setPreferenceInferrer(new AveragingPreferenceInferrer(model));
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, userSimilarity, model);
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
        Recommender cachingRecommender = new CachingRecommender(recommender);
        List<RecommendedItem> recommendations = cachingRecommender.recommend("Ken", 10);
        for (RecommendedItem r : recommendations) {
            System.out.println(r.toString());
        }
    }
}
