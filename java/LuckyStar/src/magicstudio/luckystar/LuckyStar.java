package magicstudio.luckystar;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.lcdui.*;

/**
 * User: zhou_xiaodan
 * Date: 2004-5-22
 * Time: 21:23:19
 * TODO: 
 * -- Add 'Advanced' feature
 * -- dynamic help via http
 * -- remember the last accessed case, as well as other preferences
 * -- popup confirmation before delete case/alternative
 * -- GUI enhancement: highlight 'ask' command; group similiar commands
 */
public class LuckyStar extends MIDlet {

    private MainForm mainForm;
    private MIDlet midlet = this;

    public void startApp() throws MIDletStateChangeException {
        CaseManager caseManager = CaseManager.getInstance();
        if (caseManager.size() == 0) {
            createExamples();
        } else {
            mainForm = new MainForm(this);
            Display.getDisplay(this).setCurrent(mainForm);
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean b) throws MIDletStateChangeException {
        mainForm = null;
        CaseManager.getInstance().flushAndClose();
    }

    private void createExamples() {
        Alert alert = new Alert("Create Examples?",
                "It seems that this is your first time asking Prof. Sybill questions.Do you want to see some examples?",
                null, AlertType.INFO);
        alert.setTimeout(500000);
        final Command yesCommand = new Command("Yes, please", Command.OK, 1);
        final Command noCommand = new Command("No, thanks", Command.CANCEL, 1);
        alert.addCommand(yesCommand);
        alert.addCommand(noCommand);
        alert.setCommandListener(new CommandListener() {
            public void commandAction(Command command, Displayable displayable) {
                if (command == yesCommand) {
                    CaseManager caseManager = CaseManager.getInstance();
                    Case c;

                    // first example
                    c = caseManager.createDefaultCase();
                    c.setTitle("What to do to win her heart?");
                    c.addAlternative(new Alternative("Buy her roses"));
                    c.addAlternative(new Alternative("Invite her for dinner"));
                    c.addAlternative(new Alternative("Ask her for a movie"));
                    c.addAlternative(new Alternative("Pray for god 7*24"));
                    c.addAlternative(new Alternative("No way. Stop dreaming"));

                    // second example
                    c = caseManager.createDefaultCase();
                    c.setTitle("Toss a coin, please?");
                    c.addAlternative(new Alternative("Head"));
                    c.addAlternative(new Alternative("Tail"));

                    // third example
                    c = caseManager.createDefaultCase();
                    c.setTitle("Who shall wash the dishs tonight?");
                    c.setEven(false);
                    c.addAlternative(new Alternative(3, "Mr.Husband"));
                    c.addAlternative(new Alternative(2, "Mrs.Wife"));
                    c.addAlternative(new Alternative(1, "Little Mike"));
                    c.addAlternative(new Alternative(1, "Grandpa"));
                    mainForm = new MainForm(midlet);
                    Display.getDisplay(midlet).setCurrent(mainForm);
                } else if (command == noCommand) {
                    mainForm = new MainForm(midlet);
                    Display.getDisplay(midlet).setCurrent(mainForm);
                }
            }
        });
        Display.getDisplay(this).setCurrent(alert);
    }

}