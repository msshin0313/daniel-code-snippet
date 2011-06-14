import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.similarity.UserSimilarity
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.impl.similarity.AveragingPreferenceInferrer
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

def model = new FileDataModel(new File("grouplensratings.csv"))
def userSimilarity = new PearsonCorrelationSimilarity(model)
//userSimilarity.setPreferenceInferrer(new AveragingPreferenceInferrer())
def neighborhood = new NearestNUserNeighborhood(3, userSimilarity, model)
def recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity)
def cachingRecommender = new CachingRecommender(recommender)
def recommendations = cachingRecommender.recommend("Ken", 10)

recommendations.each {
  println it;
}