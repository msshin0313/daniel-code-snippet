// producer不能生产超过3个
// 显示了使用两个lock，及之间的通信

import java.util.*;

class Consumer {
	// 这里用sync是因为同一时间只能有一个线程访问，否则中间的判断可能就不对了
	void consume( String name ) {
		synchronized ( this ) {
			if ( Main.piece == 0 ) {
				System.out.println( name + " Waiting for production" );
				try { wait(); } catch(Exception e){System.out.println(e);}
			}
			System.out.println( name + " Consuming: " + Main.piece );
			Main.piece--;
			// 这个sync block必须在Main.piece--之后，否则一旦notify后，producer就可以执行，从而piece>3
			synchronized ( Main.producer ) {
				if ( Main.piece == 2 ) {	// 消费之后==2，就是消费之前==3，就是可能有producer blocked
					// 唤醒一个block的ProducingMan
					try { Main.producer.notify(); } catch(Exception e){System.out.println(e);}
				}
			}
		}
	}
}

class Producer {
	void produce( String name ) {
		synchronized ( this ) {
			if ( Main.piece == 3 ) {
				System.out.println( name + " Waiting for consumption" );
				try { wait(); } catch(Exception e){System.out.println(e);}
			}
			Main.piece++;
			System.out.println( name + " Producing: " + Main.piece );
			synchronized ( Main.consumer ) {
				if ( Main.piece == 1 ) {
					try { Main.consumer.notify(); } catch(Exception e){System.out.println(e);}
				}
			}
		}
	}
}

class ConsumingMan extends Thread {
	public void run() {
		for ( int i=0; i<5; i++ ) {
			try { sleep(Main.rand.nextInt(2000)); } catch(Exception e){System.out.println(e);}
			Main.consumer.consume( getName() );
		}
	}
}

class ProducingMan extends Thread {
	public void run() {
		for ( int i=0; i<5; i++ ) {
			try { sleep(Main.rand.nextInt(500)); } catch(Exception e){System.out.println(e);}
			Main.producer.produce( getName() );
		}
	}
}

class Main {
	public static void main( String[] args ) {
		ProducingMan p1 = new ProducingMan(), p2 = new ProducingMan();
		ConsumingMan c1 = new ConsumingMan(), c2 = new ConsumingMan();
		p1.start(); p2.start(); 
		c1.start(); c2.start();
	}
	public static Random rand = new Random();
	public static int piece = 0;
	public static Consumer consumer = new Consumer();
	public static Producer producer = new Producer();
}