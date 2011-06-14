package magicstudio.luckystar;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import java.util.Enumeration;
import java.io.IOException;

/**
 * User: zhou_xiaodan
 * Date: 2004-5-22
 * Time: 21:23:48
 */
public class MainForm extends Form implements CommandListener, ItemStateListener {

    private static final int ALTERNATIVES_BEGIN_INDEX = 1;
    private MIDlet midlet;
    private MainForm mainform = this;

    private ChoiceGroup caseSelector; // the index should be synchronized with CaseManager
    private final Command addAlternativeCommand = new Command("Add", "Add Alternative", Command.SCREEN, 10);
    private final Command deleteAlternativeCommand = new Command("Del", "Delete Alternative", Command.SCREEN, 12);
    private final Command editAlternativeCommand = new Command("Edit", "Edit Alternative", Command.SCREEN, 11);
    private final Command resultCommand = new Command("Ask!", "Ask Professor", Command.SCREEN, 5);
    private final Command addCaseCommand = new Command("Post Q", "Post New Question", Command.SCREEN, 15);
    private final Command deleteCaseCommand = new Command("Remove Q", "Remove This Question", Command.SCREEN, 17);
    private final Command editCaseCommand = new Command("Edit Q", "Edit This Question", Command.SCREEN, 16);
    private final Command helpCommand = new Command("Intro", "Introduction", Command.HELP, 20);
    private final Command aboutCommand = new Command("About", "About this application", Command.HELP, 21);

    public MainForm(MIDlet m) {
        super("Prof.Sybill");
        midlet = m;
        init();
    }

    public void init() {
        CaseManager caseManager = CaseManager.getInstance();
        caseSelector = new ChoiceGroup("Your Question\n", Choice.POPUP, caseManager.caseTitles(), null);
        //selectedCaseIndex = caseSelector.getSelectedIndex();

        append(caseSelector);
        updateAlternatives();
        addCommand(resultCommand);
        addCommand(addAlternativeCommand);
        addCommand(editAlternativeCommand);
        addCommand(deleteAlternativeCommand);
        addCommand(addCaseCommand);
        addCommand(deleteCaseCommand);
        addCommand(editCaseCommand);
        addCommand(helpCommand);
        addCommand(aboutCommand);
        setItemStateListener(this);
        setCommandListener(this);
    }

    private void updateAlternatives() {
        // delete old alternatives
        for (int i=this.size()-1; i>=ALTERNATIVES_BEGIN_INDEX; i--) {
            delete(i);
        }
        // append new alternatives
        if (currentCase()==null) return;
        Enumeration e = currentCase().getAlternatives();
        while (e.hasMoreElements()) {
            Alternative a = (Alternative)e.nextElement();
            append( new AlternativeItem(a) );
        }
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == addAlternativeCommand) {
            if (currentCase()==null) return;
            Alternative a = new Alternative();
            currentCase().addAlternative(a);
            updateAlternatives();

        } else if (command == deleteAlternativeCommand) {
            Display.getDisplay(midlet).setCurrent(new DeleteAlternativeForm(midlet, currentCase(), mainform));

        } else if (command == editAlternativeCommand) {
            Display.getDisplay(midlet).setCurrent(new EditAlternativeForm(midlet, currentCase(), mainform));

        } else if (command == resultCommand) {
            if (currentCase()==null) return;
            Alternative a = currentCase().randomResult();
            String info;
            if ( a == null ) {
                info = "You don't have any valid alternatives, my dear";
            } else {
                info = a.getDescription();
            }
            Image img = null;
            try {
                img = Image.createImage("/face.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert resultInfo = new Alert("Answer from Prof.Sybill", info, img, AlertType.INFO);
            resultInfo.setTimeout(Alert.FOREVER);
            Display.getDisplay(midlet).setCurrent(resultInfo);

        } else if (command == addCaseCommand) {
            Case c = CaseManager.getInstance().createDefaultCase();
            int index = caseSelector.append(c.getTitle(), null);
            caseSelector.setSelectedIndex(index, true);  // updateAlternatives() should be invoked by state-change listener
            updateAlternatives();
            Display.getDisplay(midlet).setCurrent(new EditCaseForm(midlet, currentCase(), this));

        } else if (command == deleteCaseCommand) {
            if (currentCase()==null) return;
            CaseManager.getInstance().deleteCase( currentCase() );
            caseSelector.delete( caseSelector.getSelectedIndex() );
            updateAlternatives();
            //TODO: if all cases are deleted, what's next?

        } else if (command == editCaseCommand) {
            if (currentCase()==null) return;
            Display.getDisplay(midlet).setCurrent(new EditCaseForm(midlet, currentCase(), this));

        } else if (command == helpCommand) {
            String helpString = midlet.getAppProperty("HelpString");
            Alert alert = new Alert("Introduction", helpString, null, AlertType.INFO);
            alert.setTimeout(Alert.FOREVER);
            Display.getDisplay(midlet).setCurrent(alert);

        } else if (command == aboutCommand) {
            String aboutInfo = "This great app is brought to you by Xiaodan Zhou of Hogwarts China Branch. Email: danj@263.net";
            Image img = null;
            try {
                img = Image.createImage("/about.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert alert = new Alert("About", aboutInfo, img, AlertType.INFO);
            alert.setTimeout(Alert.FOREVER);
            Display.getDisplay(midlet).setCurrent(alert);
        }
    }

    public void itemStateChanged(Item item) {
        if (item == caseSelector) {
            updateAlternatives();
        } else if (item instanceof AlternativeItem) {
            AlternativeItem ai = (AlternativeItem)item;
            currentCase().setDirty(true);
            ai.getAlternative().setDescription( ai.getString() );
        }
    }

    private class AlternativeItem extends TextField {
        private static final int TEXTFIELD_MAXSIZE = 50;
        public Alternative getAlternative() {
            return alternative;
        }
        private Alternative alternative;
        public AlternativeItem(Alternative alter) {
            super(percentage(alter.getWeight()), alter.getDescription(), TEXTFIELD_MAXSIZE, TextField.ANY);
            alternative = alter;
            /*addCommand(deleteAlternativeCommand);
            setDefaultCommand(editAlternativeCommand);
            setItemCommandListener(new ItemCommandListener() {
                public void commandAction(Command command, Item item) {
                    if (command == deleteAlternativeCommand) {
                        AlternativeItem ai = (AlternativeItem) item;
                        currentCase().deleteAlternative(ai.getAlternative());
                        updateAlternatives();
                    } else if (command == editAlternativeCommand) {
                        AlternativeItem ai = (AlternativeItem) item;
                        Display.getDisplay(midlet).setCurrent(new EditAlternativeForm(midlet, currentCase(), mainform, ai.getAlternative()));
                    }
                }
            });*/
        }
    }

    private String percentage(int weight) {
        if (currentCase().isEven()) {
            return String.valueOf( 100 / currentCase().size() )+'%';
        } else {
            if (currentCase().totalWeight()==0) return "0%";
            else return String.valueOf( weight*100 / currentCase().totalWeight() )+'%';
        }
    }

    public Case currentCase() {
        int index = caseSelector.getSelectedIndex();
        if (index==-1) return null;
        else return CaseManager.getInstance().caseAt(index);
    }

    public void updateAfterEdit(String newCaseTitle) {
        if (newCaseTitle!=null)
            caseSelector.set(caseSelector.getSelectedIndex(),newCaseTitle,null);
        updateAlternatives();
    }
}
