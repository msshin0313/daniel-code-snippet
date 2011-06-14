package magicstudio.luckystar;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import java.util.Enumeration;
import java.util.Vector;

/**
 * User: zhou_xiaodan
 * Date: 2004-5-27
 * Time: 9:25:53
 */
public class DeleteAlternativeForm extends Form implements CommandListener{

    private MIDlet midlet;
    private Case theCase;
    private MainForm parent;

    private ChoiceGroup alternativeChoiceGroup = new ChoiceGroup("Alternatives", Choice.MULTIPLE);
    private Command deleteCommand = new Command("Delete", "Delete Selected Alternatives", Command.SCREEN, 1);
    private Command cancelCommand = new Command("Cancel", "Cancel", Command.CANCEL, 1);

    public DeleteAlternativeForm(MIDlet m, Case c, MainForm p) {
        super("Edit Alternatives");
        if (m==null || c==null || p==null) throw new IllegalArgumentException();
        midlet = m;
        theCase = c;
        parent = p;

        Enumeration e = theCase.getAlternatives();
        while (e.hasMoreElements()) {
            Alternative a = (Alternative)e.nextElement();
            alternativeChoiceGroup.append(a.getDescription(), null);
        }

        append(alternativeChoiceGroup);
        addCommand(deleteCommand);
        addCommand(cancelCommand);
        setCommandListener(this);
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == deleteCommand) {
            Vector deleted = new Vector();
            for (int i=0; i<alternativeChoiceGroup.size(); i++) {
                if (alternativeChoiceGroup.isSelected(i)) {
                    // when delete an element, the index will be re-ordered. So, cache the alternatives to be deleted
                    deleted.addElement( theCase.alternativeAt(i) );
                }
            }
            Enumeration e = deleted.elements();
            while (e.hasMoreElements()) {
                theCase.deleteAlternative( (Alternative)e.nextElement() );
            }
            parent.updateAfterEdit(null);
            Display.getDisplay(midlet).setCurrent(parent);
        } else if (command == cancelCommand) {
            Display.getDisplay(midlet).setCurrent(parent);
        }
    }
}
