package magicstudio.luckystar;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Random;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

/**
 * User: zhou_xiaodan
 * Date: 2004-5-22
 * Time: 21:19:14
 */
public class Case {
    private final static Random random = new Random();
    private String title;
    private boolean even;
    private boolean advanced; // not used

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public String getMagicword() {
        return magicword;
    }

    public void setMagicword(String magicword) {
        this.magicword = magicword;
    }

    private String magicword;
    private final Vector alternatives = new Vector();

    public Case(int recordId) {
        this.recordId = recordId;
        setDirty(true);
        setTitle("");
        setEven(false);
        setAdvanced(false);
        setMagicword("");
        //addAlternative(new Alternative());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isEven() {
        return even;
    }

    public void setEven(boolean even) {
        this.even = even;
    }

    public void addAlternative(Alternative alternative) {
        alternatives.addElement(alternative);
    }

    public void deleteAlternative(Alternative alternative) {
        alternatives.removeElement(alternative);
    }

    public int totalWeight() {
        int totalWeight = 0;
        Enumeration e = alternatives.elements();
        while (e.hasMoreElements()) {
            Alternative c = (Alternative)e.nextElement();
            totalWeight += c.getWeight();
        }
        return totalWeight;
    }

    public Enumeration getAlternatives() {
        return alternatives.elements();
    }

    public void saveToStream(DataOutputStream outputStream) throws IOException{
        outputStream.writeUTF(title);
        outputStream.writeBoolean(even);
        outputStream.writeBoolean(advanced);
        outputStream.writeUTF(magicword);
        Enumeration e = alternatives.elements();
        while (e.hasMoreElements()) {
            Alternative a = (Alternative)e.nextElement();
            a.saveToStream(outputStream);
        }
    }

    public static Case createFromStream(int recordId, DataInputStream inputStream) throws IOException {
        Case c = new Case(recordId);
        c.setDirty(false); // not dirty, no need to flush
        c.setTitle(inputStream.readUTF());
        c.setEven(inputStream.readBoolean());
        c.setAdvanced(inputStream.readBoolean());
        c.setMagicword(inputStream.readUTF());
        while ( inputStream.available() != 0 ) {
            c.addAlternative( Alternative.createFromStream(inputStream) );
        }
        return c;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    private boolean dirty = true;

    public int getRecordId() {
        return recordId;
    }

    private int recordId;

    /**
     * Using some random algorithm to get the result
     * @return the chosen alternative, or null is no alternatives available.
     */
    public Alternative randomResult() {
        if (size() == 0) return null;

        // magicword functionality. development status: exprimental
        if (getMagicword()!=null) {
            // magic word: mamamia returns the first element in 80% possibility
            if (getMagicword().toLowerCase().equals("mamamia")) {
                if (random.nextInt()%10 < 8) {
                    for (int i=0; i<alternatives.size(); i++) {
                        Alternative a = alternativeAt(i);
                        if (a.getWeight() != 0) return a;
                    }
                }
            // magic word: sesame returns the last element in 80% possibility
            } else if (getMagicword().toLowerCase().equals("sesame")) {
                if (random.nextInt()%10 < 8) {
                    for (int i=alternatives.size()-1; i>=0; i--) {
                        Alternative a = alternativeAt(i);
                        if (a.getWeight() != 0) return a;
                    }
                }
            }
        }

        if (even) {
            int randInt = random.nextInt() % size();
            if (randInt<0) randInt += size();
            return (Alternative)alternatives.elementAt(randInt);
        } else {
            if (totalWeight() == 0) return null;
            int randInt = random.nextInt() % totalWeight();
            if (randInt<0) randInt += totalWeight();
            Enumeration e = alternatives.elements();
            while (e.hasMoreElements()) {
                Alternative a = (Alternative)e.nextElement();
                randInt -= a.getWeight();
                if (randInt<0) return a;
            }
            throw new Error("Internal Error"); // never reach here
        }
    }

    public Alternative alternativeAt(int index) {
        return (Alternative)alternatives.elementAt(index);
    }

    public int size() {
        return alternatives.size();
    }
}
