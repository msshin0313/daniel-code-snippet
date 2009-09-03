# Multiarmed Bandit Simulation -- Original

import random

class Sample:
    w = 1
    p = 0
    x = 0

class SampleList:
    list = []
    gamma = 0

    def __init__(self, gamma = 1, K = 100):
        assert gamma<=1 and gamma>0 and K>0
        self.gamma = gamma
        for i in range(K):
            s = Sample()
            s.w = 1
            self.list.append(s)

    def sumW(self):
        sum = 0
        for s in self.list:
            sum += s.w
        return sum

    def updateP(self):
        sum = self.sumW()
        K = len(self.list)
        for s in self.list:
            s.p = (1-self.gamma) * s.w / sum + self.gamma / K

    def randomDraw(self):
        it = random.random()


def main():
    pass

if __name__ == '__main__':
    main()