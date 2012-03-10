/*
grouplens data in the paper
user, item, rating
1,1,1
1,2,5
1,4,2
1,5,4
2,1,4
2,2,2
2,4,5
2,5,1
2,6,2
3,1,2
3,2,4
3,3,3
3,6,5
4,1,2
4,2,4
4,4,5
4,5,1

 */

import org.apache.commons.dbcp.BasicDataSource
import org.apache.commons.dbcp.BasicDataSourceFactory
import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.model.JDBCDataModel
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood
import org.apache.mahout.cf.taste.recommender.RecommendedItem
import org.apache.mahout.cf.taste.similarity.ItemSimilarity
import org.apache.mahout.cf.taste.similarity.UserSimilarity
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator

def User2User() {
    DataModel model = new FileDataModel(new File("data.txt"));
    UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
    //userSimilarity.setPreferenceInferrer(new AveragingPreferenceInferrer());
    UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, userSimilarity, model);
    GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
    //Recommender cachingRecommender = new CachingRecommender(recommender);
    //List<RecommendedItem> recommendations = cachingRecommender.recommend(1234, 10);

    // result should be 4.56 according to the paper
    // doesn't turn out to be 4.56: could be the PearsonCorrelation uses the missing data differently.
    println recommender.estimatePreference(1, 6)
}

def Item2Item() {
    DataModel model = new FileDataModel(new File("data.txt"));
    //Collection<GenericItemSimilarity.ItemItemSimilarity> correlations = ...;
    //ItemSimilarity itemSimilarity = new GenericItemSimilarity(correlations);
    ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
    GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);
    //Recommender cachingRecommender = new CachingRecommender(recommender);
    //List<RecommendedItem> recommendations = cachingRecommender.recommend(1234, 10);
    println recommender.estimatePreference(1, 6)
}

def JDBCItem2Item() {
    // SQLite JDBC has problems. And can't work
    Properties dbProperties = new Properties();
    //dbProperties.put("driverClassName", "org.sqlite.JDBC")
    //dbProperties.put("url", "jdbc:sqlite:/home/daniel/Development/snippet/java/mahout/sqlite.db")

    dbProperties.put("driverClassName", "com.mysql.jdbc.Driver")
    dbProperties.put("url", "jdbc:mysql://localhost/dev")
    dbProperties.put("username", "dev")
    dbProperties.put("password", "dev")

    BasicDataSource dataSource = BasicDataSourceFactory.createDataSource(dbProperties);
    //dataSource.setMaxActive(1)
    JDBCDataModel model = new MySQLJDBCDataModel(dataSource, "ratings", "user", "item", "rating", "updated");

    ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
    GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);
    println recommender.estimatePreference(1, 6);
    recommender.refresh(null);
}


// not working code. pseudo code!!!
def RecommenderInitialUpdateOffline() {
    // first-time running the recommender
    JDBCDataModel model = new MySQLJDBCDataModel(dataSource, "ratings", "user_id", "item_id", "rating", "updated");
    ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
    GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);
    // print a prediction score for user-item.
    println recommender.estimatePreference(user_id, item_id);

    // compute the initial item-similarity data and persistence them. time-consuming.
    LongPrimitiveIterator iterator = model.getItemIDs();
    while (iterator.hasNext()) {
        long itemID1 = iterator.nextLong();
        for (RecommendedItem item : recommender.mostSimilarItems(itemID1, maxKeep)) {
            long itemID2 = item.getItemID();
            double score = item.getValue();
            // save the item-simiarity data in file or database.
            saveSimilarity(itemID1, itemID2, score);
        }
    }
}

def RecommenderIncrementalUpdate() {
    // in incremental update, we still need to initialize these objects to compute similarity/preference data for new users/items
    JDBCDataModel model = new MySQLJDBCDataModel(dataSource, "ratings", "user_id", "item_id", "rating", "updated");
    ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
    GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);

    // incrementally compute and save similarity data for new items.
    // existing similarity data is relatively stable, and we won't touch them. thus we can save lots of computation time.
    List<Long> newItems = loadNewItems();
    for (long itemID1 : newItems) {
        for (RecommendedItem item : recommender.mostSimilarItems(itemID1, maxKeep)) {
            long itemID2 = item.getItemID();
            double score = item.getValue();
            // append the new item-simiarity data in file or database.
            saveSimilarity(itemID1, itemID2, score);
        }
    }

    // reload all similarity data from persistent storage.
    List<GenericItemSimilarity.ItemItemSimilarity> ii = loadSimilarity();
    GenericItemSimilarity reloadSimilarity = new GenericItemSimilarity(ii);
    GenericItemBasedRecommender reloadRecommender = new GenericItemBasedRecommender(model, reloadSimilarity);
    // print a prediction score for user-item
    println recommender.estimatePreference(user_id, item_id);
}

/**
 * see https://cwiki.apache.org/confluence/display/MAHOUT/Recommender+Documentation
 */
def Evaluation() {
    DataModel dataModel = new FileDataModel(new File("data.txt"));
    RecommenderBuilder userBasedRecommenderBuilder = new RecommenderBuilder() {
        public Recommender buildRecommender(DataModel model) {
            UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, userSimilarity, model);
            GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
            return recommender;
        }
    }
    RecommenderBuilder itemBasedRecommenderBuilder = new RecommenderBuilder() {
        public Recommender buildRecommender(DataModel model) {
            ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
            GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);
            return recommender;
        }
    }
    RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
    println evaluator.evaluate(itemBasedRecommenderBuilder, null, dataModel, 0.5, 0.5);
}


//JDBCItem2Item()
Evaluation()