# Multiarmed Bandit Simulation

class Sample:
    w = 1
    p = 0
    x = 0
    trueP = 0
    clicked = false

class SampleList:
    list = []
    gamma = 0

    def __init__(self, gamma = 1):
        assert gamma<=1 and gamma>0
        self.gamma = gamma

    def loadTrueP(self, fileName):
        input = open(fileName, 'r')
        for line in input:
            s = Sample();
            s.trueP = float(line)
            s.w = 1
            self.list.append(s)

    def sumW(self):
        sum = 0
        for s in list:
            sum += s.w
        return sum

    def updateP(self):
        sum = self.sumW()
        K = len(self.list)
        for s in self.list:
            s.p = (1-self.gamma) * s.w / sum + self.gamma / K

    def drawTop(self, num = 1):
        results = list.sort(lambda x,y: x.trueP-y.trueP)[0:num-1]
        return results

    def randomClick(self, num = 1):
        results = self.drawTop(num)
        for s in results:
            


def main():
    pass

if __name__ == '__main__':
    main()