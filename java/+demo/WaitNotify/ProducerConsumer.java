// procuder可以生产任意多，但consumer消费到0时就只能等待

import java.util.*;

class Food {
	private int piece = 0;
	synchronized void produce( String name ) {
		if ( piece == 0 ) {
			try { notify(); } catch(Exception e){System.out.println(e);}
		}
		piece++;
		System.out.println( name + " Producing: " + piece );
	}
	synchronized void consume( String name ) {
		if ( piece == 0 ) {
			System.out.println( name + " Wait for food" );
			try { wait(); } catch(Exception e){System.out.println(e);}
		}
		System.out.println( name + " Consuming: " + piece );
		piece--;
	}
}

class Producer extends Thread {
	public void run() {
		for ( int i=0; i<5; i++ ) {
			try { sleep(Main.rand.nextInt(2000)); } catch(Exception e){System.out.println(e);}
			Main.food.produce( getName() );
		}
	}
}

class Consumer extends Thread {
	public void run() {
		for ( int i=0; i<5; i++ ) {
			try { sleep(Main.rand.nextInt(1000)); } catch(Exception e){System.out.println(e);}
			Main.food.consume( getName() );
		}
	}
}

class Main {
	public static void main( String[] args ) {
		Producer p1 = new Producer(), p2 = new Producer();
		Consumer c1 = new Consumer(), c2 = new Consumer();
		p1.start(); p2.start(); 
		c1.start(); c2.start();
	}
	public static Food food = new Food();
	public static Random rand = new Random();
}