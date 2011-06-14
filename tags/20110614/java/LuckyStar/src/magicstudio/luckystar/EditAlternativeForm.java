package magicstudio.luckystar;

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import java.util.Enumeration;

/**
 * User: zhou_xiaodan
 * Date: 2004-5-26
 * Time: 16:12:33
 */
public class EditAlternativeForm extends Form implements CommandListener, ItemStateListener {
    private MIDlet midlet;
    private Case theCase;
    private MainForm parent;

    private ChoiceGroup alternativeChoiceGroup = new ChoiceGroup("Alternatives", Choice.EXCLUSIVE);
    private StringItem weightInfoStringItem = new StringItem(null, "");
    private Gauge adjustWeightGauge = new Gauge("Adjust Galleons", true, 20, 0);

    private Command okCommand = new Command("OK", "OK, remember modification", Command.OK, 1);

    public EditAlternativeForm(MIDlet m, Case c, MainForm p) {
        super("Edit Alternatives");
        if (m==null || c==null || p==null) throw new IllegalArgumentException();
        midlet = m;
        theCase = c;
        parent = p;
        load();
        append(alternativeChoiceGroup);
        append(weightInfoStringItem);
        append(adjustWeightGauge);
        setTicker(new Ticker("More Galleons More Chances"));
        addCommand(okCommand);
        setCommandListener(this);
        setItemStateListener(this);
    }

    private void load() {
        Enumeration e = theCase.getAlternatives();
        while (e.hasMoreElements()) {
            Alternative a = (Alternative)e.nextElement();
            alternativeChoiceGroup.append(a.getDescription(), null);
        }
        updateCurrentAlternativeDisplay();
    }

    private Alternative currentAlternative() {
        int index = alternativeChoiceGroup.getSelectedIndex();
        if (index == -1) return null;
        else return theCase.alternativeAt(index);
    }

    /**
     * save the modified infomation into the case instance.
     */
    private void save() {
        if (theCase.isDirty()) {
            parent.updateAfterEdit(null);
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
        if (item != alternativeChoiceGroup) theCase.setDirty(true);
        if (item == adjustWeightGauge) {
            Alternative a = currentAlternative();
            if (a!=null) {
                int gaugeValue = ((Gauge)item).getValue();
                a.setWeight(gaugeValue);
                updateCurrentAlternativeDisplay();
            }
        } else if (item == alternativeChoiceGroup) {
            updateCurrentAlternativeDisplay();
        }
    }

    private void updateCurrentAlternativeDisplay() {
        Alternative a = currentAlternative();
        if (a==null) {
            weightInfoStringItem.setText("");
            adjustWeightGauge.setValue(0);
        } else {
            weightInfoStringItem.setText(""+a.getWeight()+" Galleons, "+percentage(a.getWeight())+" Chances");
            adjustWeightGauge.setValue(a.getWeight());
        }
    }

    private String percentage(int weight) {
        if (theCase.totalWeight()==0) return "0%";
        else return String.valueOf( weight*100 / theCase.totalWeight() )+'%';
    }
}