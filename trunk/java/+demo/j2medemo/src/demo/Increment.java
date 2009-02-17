package demo;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.lcdui.*;

public class Increment extends MIDlet implements CommandListener,ItemCommandListener{
    private int value = 0;
    private Form mainForm;
    private final static Command CMD_ADD = new Command("Add", Command.ITEM, 1);
    private final static Command CMD_SUB = new Command("Sub", Command.ITEM, 1);
    private final static StringItem label = new StringItem("Accessed: ", "0");
    private final static StringItem addButton = new StringItem("Add", null, Item.BUTTON);
    private final static StringItem subButton = new StringItem("Sub", null, Item.BUTTON);

    public Increment() {
        mainForm = new Form("Increment App");
        mainForm.append(label);
        mainForm.setTicker( new Ticker("Have a nice day! :)") );
        addButton.setDefaultCommand(CMD_ADD);
        subButton.setDefaultCommand(CMD_SUB);
        //subButton.setLayout(Item.LAYOUT_RIGHT);
        addButton.setItemCommandListener(this);
        subButton.setItemCommandListener(this);
        addButton.setPreferredSize(addButton.getPreferredWidth()*2,-1);
        subButton.setPreferredSize(subButton.getPreferredWidth()*2,-1);
        mainForm.append(addButton);
        mainForm.append(subButton);
        //mainForm.addCommand(CMD_ADD);
        //mainForm.addCommand(CMD_SUB);
        //mainForm.setCommandListener(this);
    }

    public void startApp() throws MIDletStateChangeException {
        Display.getDisplay(this).setCurrent(mainForm);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean b) throws MIDletStateChangeException {
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == CMD_ADD) {
            value++;
            label.setText(String.valueOf(value));
        } else if (command == CMD_SUB) {
            value--;
            label.setText(String.valueOf(value));
        }
    }

    public void commandAction(Command command, Item item) {
        System.out.println("ItemCommandListener Invoked.");
        commandAction(command, (Displayable) null);
    }

}
