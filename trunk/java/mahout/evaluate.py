from org.apache.mahout.cf.taste.impl.model.file import FileDataModel
from org.apache.mahout.cf.taste.impl.similarity import PearsonCorrelationSimilarity
from org.apache.mahout.cf.taste.impl.neighborhood import NearestNUserNeighborhood
from org.apache.mahout.cf.taste.impl.recommender import GenericUserBasedRecommender, GenericItemBasedRecommender
from org.apache.mahout.cf.taste.impl.recommender.svd import ALSWRFactorizer, SVDRecommender
from java.io import File

f = File('data.txt')
model = FileDataModel(f)

## user-user
#userSimilarity = PearsonCorrelationSimilarity(model)
#neighborhood = NearestNUserNeighborhood(10, userSimilarity, model)
#recommender = GenericUserBasedRecommender(model, neighborhood, userSimilarity)

# item-item
#itemSimilarity = PearsonCorrelationSimilarity(model)
#recommender = GenericItemBasedRecommender(model, itemSimilarity)

# svd
factorizer = ALSWRFactorizer(model, 3, 0.05, 50)
recommender = SVDRecommender(model, factorizer)

print recommender.estimatePreference(1, 6)

