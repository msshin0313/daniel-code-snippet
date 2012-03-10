# has to go with Python 2.x for now.

"""
This is the file for the semi-supervised "graph propagation" algorithm proposed in Zhou, Resnick & Mei 2011.
See more details in http://www.aaai.org/ocs/index.php/ICWSM/ICWSM11/paper/viewFile/2782/3293
"""

class Classifier(object):
  """
  Default class for all classifiers. Read file, etc.
  """

  def __init__(self, link_file, label_file, result_file, alpha = 0.2, tolerance = 0.001):
    pass

  def classify(self):
    pass

  def evaluate(self, training, testing):
    pass

  def cross_validate(self):
    pass



class RWR(Classifier):
  """
  Random walk with restart algorithm.
  """
  pass



class LCGC(Classifier):
  """
  Local consistency global consistency algorithm.
  """
  pass




class ARW(Classifier):
  """
  Absorbing random walk algorithm
  """
  pass
