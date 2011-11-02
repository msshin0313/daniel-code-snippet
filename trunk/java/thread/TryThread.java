package thread;

public class TryThread {

    private static Thread t1;
    private static Thread t2;

    public static void main(String[] args) {
        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread 1 is running.");
                try {
                    t2.join();
                } catch (InterruptedException e) {}
                System.out.println("Thread 1 done");
            }
        });

        t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread 2 is running");
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {}
                System.out.println("Thread 2 done");
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
        } catch (InterruptedException e) { }
        System.out.println("Main finished");
    }
}
