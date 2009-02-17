package magicstudio.util;

/**
 * Date: 2004-1-10
 * Time: 20:36:46
 * Desc: used to store or parseString single key-value pair, e.g. "FullScreen=true", "Name=dan", "Unit=5", etc.
 *       allow value to be 'null'
 * Progress: Almost done, need to consider more functionality about getValueInteger() etc.
 */

import junit.framework.TestCase;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class KeyValuePair {
    private String _key;
    private Object _value;

    public KeyValuePair(String key, Object value) {
        assert key != null;
        _key = key;
        _value = value;
    }

    public static KeyValuePair parseString(String strPair) throws IllegalInputException {
        assert strPair != null;
        Matcher m = Pattern.compile("^([^=]+)=([^=]*)$").matcher(strPair);
        if ( !m.matches() ) throw new IllegalInputException("Illegal input format");
        else {
            return new KeyValuePair(m.group(1), m.group(2));
        }
    }

    public static KeyValuePair parseInteger(String strPair) throws IllegalInputException {
        assert strPair != null;
        KeyValuePair kvp = parseString(strPair);
        try {
            kvp._value = Integer.valueOf( (String)kvp._value );
        } catch (NumberFormatException e) {
            throw new IllegalInputException("Can not parse integer");
        }
        return kvp;
    }

    public static KeyValuePair parseBoolean(String strPair) throws IllegalInputException {
        assert strPair != null;
        KeyValuePair kvp = parseString(strPair);
        if (
                ( (String)kvp._value ).toLowerCase().equals("true") ||
                ( (String)kvp._value ).toLowerCase().equals("yes") ||
                ( (String)kvp._value ).toLowerCase().equals("y")
            ) kvp._value = Boolean.TRUE;
        else if (
                ( (String)kvp._value ).toLowerCase().equals("false") ||
                ( (String)kvp._value ).toLowerCase().equals("no") ||
                ( (String)kvp._value ).toLowerCase().equals("n")
            ) kvp._value = Boolean.FALSE;
        else throw new IllegalInputException("Can not parse boolean");
        return kvp;
    }

    public String getKey() {
        return _key;
    }

    public Object getValue() {
        return _value;
    }

    public static class TheTest extends TestCase{

        public void testLegalParse() throws IllegalInputException {
            legalParse("b=c", "b", "c");
            legalParse("aa=ccc", "aa", "ccc");
            legalParse("43134=bbb", "43134", "bbb");
            legalParse("cc=", "cc", "");
        }

        private void legalParse(String pair, String key, String value) throws IllegalInputException {
            KeyValuePair kvp = KeyValuePair.parseString(pair);
            assertEquals(key, kvp.getKey());
            assertEquals(value, (String)kvp.getValue());
        }

        public void testIllegalParse() {
            illegalParse("cc=bb=aa");
            illegalParse("cc=bb==aa");
            illegalParse("=b");
            illegalParse("aa==bb");
            illegalParse("aa===bb");
            illegalParse("==b");
            illegalParse("=b");
            illegalParse("===");
            illegalParse("aabb");
            illegalParse("");
        }

        private void illegalParse(String s) {
            try {
                KeyValuePair kvp = KeyValuePair.parseString(s);
                fail("Should throw exception: "+s
                        +" key:"+kvp.getKey()
                        +" value:"+kvp.getValue());
            } catch (IllegalInputException e) {}
        }

    }

}
