package magicstudio.luckystar;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import java.util.Enumeration;

/**
 * User: zhou_xiaodan
 * Date: 2004-5-26
 * Time: 10:10:20
 */
public class EditCaseForm extends Form implements CommandListener, ItemStateListener {

    private MIDlet midlet;
    private Case theCase;
    private MainForm parent;

    private TextField caseTitleTextField = new TextField("Question Description", "", 100, TextField.ANY);
    private ChoiceGroup isEvenChoiceGroup = new ChoiceGroup("Options", Choice.MULTIPLE, new String[]{"Force Even?"}, null);
    private TextField magicwordTextField = new TextField("Magic Word", "", 30, TextField.ANY);

    private Command okCommand = new Command("OK", "OK, remember modification", Command.OK, 1);
//    private Command cancelCommand = new Command("Cancel", "Cancel, ignore modification", Command.CANCEL, 2);

    public EditCaseForm(MIDlet m, Case c, MainForm p) {
        super("Edit Question");
        if (m==null || c==null || p==null) throw new IllegalArgumentException();
        midlet = m;
        theCase = c;
        parent = p;
        load();
        append(caseTitleTextField);
        append(isEvenChoiceGroup);
        append(magicwordTextField);

        addCommand(okCommand);
//        addCommand(cancelCommand);
        setCommandListener(this);
        setItemStateListener(this);
    }

    private void load() {
        caseTitleTextField.setString(theCase.getTitle());
        isEvenChoiceGroup.setSelectedIndex(0, theCase.isEven());
        magicwordTextField.setString(theCase.getMagicword());
    }

    /**
     * save the modified infomation into the case instance.
     */
    private void save() {
        if (theCase.isDirty()) {
            theCase.setTitle(caseTitleTextField.getString());
            theCase.setEven(isEvenChoiceGroup.isSelected(0));
            theCase.setMagicword(magicwordTextField.getString());
            parent.updateAfterEdit(caseTitleTextField.getString());
        }
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command==okCommand) {
            save();
            Display.getDisplay(midlet).setCurrent(parent);
        }/* else if (command == cancelCommand) {
            Display.getDisplay(midlet).setCurrent(parent);
        }*/
    }

    public void itemStateChanged(Item item) {
        theCase.setDirty(true);
    }

}
