/**
 * Multiarmed Bandit Simulation
 */

class Sample {
	float w;
	float p;
	float x;
	float trueP;
}
 
class SampleList {
	def list = new ArrayList<Sample>();
	def gamma;
	
	def SampleList(fileName, gamma) {
		assert gamma<=1 && gamma>0;
		this.gamma = gamma;
		
		def input = new File(fileName);
		input.eachLine { line ->
			Sample s = new Sample();
			s.w = 1; // initalize w=1 for t=1
			s.trueP = line.toFloat();
			list.add(s);
		}
	}
	
	def sumW() {
		sum = 0;
		list.each { s ->
			sum += s.w;
		}
		return sum;
	}
	
	def updateP() {
		sum = sumW();
		K = list.size();
		list.each { s ->
			s.p = (1-gamma) * (s.w / sum) + (gamma / K);
		}
	}
	
	
}

SampleList samples = new SampleList("data.csv", 0.2f);