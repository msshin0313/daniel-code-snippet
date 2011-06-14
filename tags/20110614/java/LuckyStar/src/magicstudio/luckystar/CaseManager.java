package magicstudio.luckystar;

import javax.microedition.rms.*;
import java.util.Vector;
import java.util.Enumeration;
import java.io.*;

/**
 * User: zhou_xiaodan
 * Date: 2004-5-22
 * Time: 21:19:56
 */
public class CaseManager {

    // singleton
    private static CaseManager instance;
    public static synchronized CaseManager getInstance() {
        if (instance==null) {
            instance = new CaseManager();
        }
        return instance;
    }

    // ensure that the order of the cases matches that of MainForm->caseSelector
    private Vector cases;

    private CaseManager() {
        cases = new Vector();
        try {
            caseRecordStore = RecordStore.openRecordStore("cases", true);
        } catch (RecordStoreException e) {
            //TODO: add user friendly message
            e.printStackTrace();
            System.exit(-1);
        }
        loadCases();
    }

    /**
     * Get the titles of each case.
     * @return If there are no cases, return a String[] whose contents is empty.
     */
    public String[] caseTitles() {
        String[] titles = new String[ cases.size() ];
        for (int i=0; i<cases.size(); i++) {
            Case c = (Case) cases.elementAt(i);
            titles[i] = c.getTitle();
        }
        return titles;
    }

    public int size() {
        return cases.size();
    }

    public Case caseAt(int index) {
        return (Case)cases.elementAt(index);
    }

    private RecordStore caseRecordStore;

    public Case createDefaultCase() {
        int recordId = 0;
        try {
            recordId = caseRecordStore.addRecord(new byte[0], 0, 0);
        } catch (RecordStoreException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Case c = new Case(recordId);
        c.setTitle("New Question");
        cases.addElement(c);
        return c;
    }

    public void deleteCase( Case c ) {
        if (c==null) throw new IllegalArgumentException();
        try {
            caseRecordStore.deleteRecord(c.getRecordId());
        } catch (RecordStoreException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        cases.removeElement(c);
    }

    /**
     * Flush the dirty heap data into RMS and close the RecordStore
     */
    public void flushAndClose() {
        Enumeration e = cases.elements();
        while (e.hasMoreElements()) {
            Case c = (Case)e.nextElement();
            if (c.isDirty()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream outputStream = new DataOutputStream(baos);
                try {
                    c.saveToStream(outputStream);
                    // store record
                    caseRecordStore.setRecord(c.getRecordId(),
                            baos.toByteArray(), 0, baos.toByteArray().length);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InvalidRecordIDException e1) {
                    e1.printStackTrace();
                } catch (RecordStoreNotOpenException e1) {
                    e1.printStackTrace();
                } catch (RecordStoreFullException e1) {
                    e1.printStackTrace();
                } catch (RecordStoreException e1) {
                    e1.printStackTrace();
                }
            }
        }
        try {
            caseRecordStore.closeRecordStore();
        } catch (RecordStoreException e1) {
            e1.printStackTrace();
            System.exit(-1);
        }
        instance = null;
    }

    /**
     * Load case records stored in RNS into heap, so that the following operations could be handled in memory
     */
    private void loadCases() {
        RecordEnumeration re = null;
        try {
            re = caseRecordStore.enumerateRecords(null, null, true);
        } catch (RecordStoreNotOpenException e) {
            e.printStackTrace();
        }
        while (re.hasNextElement()) {
            try {
                int recordId = re.nextRecordId();
                byte[] bytes = caseRecordStore.getRecord(recordId);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                DataInputStream inputStream = new DataInputStream(bais);
                Case c = Case.createFromStream(recordId, inputStream);
                cases.addElement(c);
            } catch (RecordStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        re.destroy();
    }
}
