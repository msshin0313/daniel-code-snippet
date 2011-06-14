// producer������������3��
// ��ʾ��ʹ������lock����֮���ͨ��

import java.util.*;

class Consumer {
	// ������sync����Ϊͬһʱ��ֻ����һ���̷߳��ʣ������м���жϿ��ܾͲ�����
	void consume( String name ) {
		synchronized ( this ) {
			if ( Main.piece == 0 ) {
				System.out.println( name + " Waiting for production" );
				try { wait(); } catch(Exception e){System.out.println(e);}
			}
			System.out.println( name + " Consuming: " + Main.piece );
			Main.piece--;
			// ���sync block������Main.piece--֮�󣬷���һ��notify��producer�Ϳ���ִ�У��Ӷ�piece>3
			synchronized ( Main.producer ) {
				if ( Main.piece == 2 ) {	// ����֮��==2����������֮ǰ==3�����ǿ�����producer blocked
					// ����һ��block��ProducingMan
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