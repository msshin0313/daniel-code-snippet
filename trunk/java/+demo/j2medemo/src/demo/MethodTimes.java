package demo;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.Display;

/**
 * Dan: From JDK MIDlet package
 * An example MIDlet runs a simple timing test
 * When it is started by the application management software it will
 * create a separate thread to do the test.
 * When it finishes it will notify the application management software
 * it is done.
 * Refer to the startApp, pauseApp, and destroyApp
 * methods so see how it handles each requested transition.
 */
public class MethodTimes extends MIDlet implements Runnable {
    // The state for the timing thread.
    Thread thread;
    private int value=0;
    private final Form form = new Form("Main Form");
    private final StringItem label = new StringItem("Paused: ", String.valueOf(value));
    private final StringItem threadNo = new StringItem("Thread #", "init");

    public MethodTimes() {
        form.append(label);
        form.append(threadNo);
    }

    /**
     * Start creates the thread to do the timing.
     * It should return immediately to keep the dispatcher
     * from hanging.
     */
    public void startApp() {
        thread = new Thread(this);
        thread.start();
        label.setText(String.valueOf(value));
        Display.getDisplay(this).setCurrent(form);
    }

    /**
     * Pause signals the thread to stop by clearing the thread field.
     * If stopped before done with the iterations it will
     * be restarted from scratch later.
     */
    public void pauseApp() {
        value++;
        thread = null;
    }

    /**
     * Destroy must cleanup everything.  The thread is signaled
     * to stop and no result is produced.
     */
    public void destroyApp(boolean unconditional) {
        thread = null;
    }

    /**
     * Run the timing test, measure how long it takes to
     * call a empty method 1000 times.
     * Terminate early if the current thread is no longer
     * the thread from the
     */
    public void run() {
        Thread curr = Thread.currentThread();  // Remember which thread is current
        threadNo.setText(String.valueOf(Thread.activeCount()));
        long start = System.currentTimeMillis();
        for (int i = 0; i < 250000 && thread == curr; i++) {
            empty();
        }
        long end = System.currentTimeMillis();

        // Check if timing was aborted, if so just exit
        // The rest of the application has already become quiescent.
        if (thread != curr) {
            return;
        }
        long millis = end - start;
        System.out.println(millis);
        // Reporting the elapsed time is outside the scope of this example.

        // All done cleanup and quit
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * An Empty method.
     */
    void empty() {
    }

}