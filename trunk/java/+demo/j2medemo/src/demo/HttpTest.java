package demo;

import java.io.*;

import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class HttpTest extends MIDlet implements CommandListener {
    private Display mDisplay;
    private Form mMainForm;
    private StringItem mMessageItem;
    private Command mExitCommand, mConnectCommand;

    public HttpTest() {
        mMainForm = new Form("HttpTest");
        mMessageItem = new StringItem(null, "");
        mExitCommand = new Command("Exit", Command.EXIT, 0);
        mConnectCommand = new Command("Connect",
                Command.SCREEN, 0);
        mMainForm.append(mMessageItem);
        mMainForm.addCommand(mExitCommand);
        mMainForm.addCommand(mConnectCommand);
        mMainForm.setCommandListener(this);
    }

    public void startApp() {
        mDisplay = Display.getDisplay(this);
        mDisplay.setCurrent(mMainForm);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {
        if (c == mExitCommand)
            notifyDestroyed();
        else if (c == mConnectCommand) {
            Form waitForm = new Form("Waiting...");
            mDisplay.setCurrent(waitForm);
            Thread t = new Thread() {
                public void run() {
                    connect();
                }
            };
            t.start();
        }
    }

    private void connect() {
        HttpConnection hc = null;
        InputStream in = null;
        String url = getAppProperty("HttpTest.URL");

        try {
            hc = (HttpConnection) Connector.open(url);
            in = hc.openInputStream();

            int contentLength = (int) hc.getLength();
            byte[] raw = new byte[contentLength];
            int length = in.read(raw);

            in.close();
            hc.close();

            // Show the response to the user.
            String s = new String(raw, 0, length);
            mMessageItem.setText(s);
        } catch (IOException ioe) {
            mMessageItem.setText(ioe.toString());
        }
        mDisplay.setCurrent(mMainForm);
    }
}

