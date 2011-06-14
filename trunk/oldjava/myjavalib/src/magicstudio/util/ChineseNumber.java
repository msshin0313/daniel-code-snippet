package magicstudio.util;

import junit.framework.TestCase;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.text.ParseException;

/**
 * Date: 2004-1-11
 * Time: 12:23:33
 * Desc: Utilities to handle chinese number system, e.g. “十五”“四百五十”等
 *       charactor includes 〇（零）一二三四五六七八九十百千万
 *       Note: Only support integer
 * Progress: ready to run, does not support 千，万
 */

public class ChineseNumber {

    private static interface ConvertStrategy {
        int convert(String chNumber);
    }

    private static Map<Pattern,ConvertStrategy> convertMap = new HashMap<Pattern,ConvertStrategy>();
    private static Map<Character,Integer> digitMap = new HashMap<Character,Integer>(20);

    private static void register(String chNumPattern, ConvertStrategy conStr) {
        Pattern p = null;
        try {
            p = Pattern.compile(chNumPattern);
        } catch (PatternSyntaxException e) {
            assert false;
        }
        convertMap.put(p, conStr);
    }

    static {
        buildDigitMap();
        buildConvertMap();
    }

    private static void buildDigitMap() {
        // j2se1.5: autoboxing
        digitMap.put('?',0);
        digitMap.put('?',0);
        digitMap.put('?',1);
        digitMap.put('?',2);
        digitMap.put('?',3);
        digitMap.put('?',4);
        digitMap.put('?',5);
        digitMap.put('?',6);
        digitMap.put('?',7);
        digitMap.put('?',8);
        digitMap.put('?',9);
    }

    private static void buildConvertMap() {
        register("[一二三四五六七八九〇零]+", new ConvertStrategy() {
            public int convert(String chNumber) {
                return convertRaw(chNumber);
            }
        });
        register("十[一二三四五六七八九]?", new ConvertStrategy() {
            public int convert(String chNumber) {
                return 10 + convertRaw(chNumber.substring(1));
            }
        });
        register("[一二三四五六七八九]十[一二三四五六七八九]?", new ConvertStrategy() {
            public int convert(String chNumber) {
                return 10*convertRaw(chNumber.charAt(0)) + convertRaw(chNumber.substring(2));
            }
        });
        register("[一二三四五六七八九]百[〇零][一二三四五六七八九]", new ConvertStrategy() {
            public int convert(String chNumber) {
                return 100*convertRaw(chNumber.charAt(0)) + convertRaw(chNumber.charAt(3));
            }
        });
        register("[一二三四五六七八九]百([一二三四五六七八九]十[一二三四五六七八九]?)?", new ConvertStrategy() {
            public int convert(String chNumber) {
                return 100*convertRaw(chNumber.charAt(0)) + recursiveParse(chNumber.substring(2));
            }
        });

        // template
        /*register("", new ConvertStrategy() {
            public int convert(String chNumber) {
                return ;
            }
        });*/
    }

    // the caller should guarantee there is no parse exception
    // used internally, for recursive use.
    private static int recursiveParse(String chNumber) {
        try {
            return parse(chNumber);
        } catch(ParseException e) {
            assert false;
            return 0; // dump return
        }
    }

    // convert raw chinese number string to int
    // e.g. "五八三" is converted to 583
    // does not support precision
    // 十、百、千、万 etc is not allowed here
    // callers should guarantee the format is legal, otherwise, an exception will be thrown
    private static int convertRaw(String chNumber) {
        if (chNumber==null || chNumber.equals("")) return 0;
        int value = 0;
        for (int i=0; i<chNumber.length(); i++) {
            value = value * 10 + convertRaw( chNumber.charAt(i) );
        }
        return value;
    }

    private static int convertRaw(char chDigit) {
        assert digitMap.containsKey(chDigit);
        return digitMap.get( chDigit );
    }

    static public int parse(String chNumber) throws ParseException {
        for ( Map.Entry<Pattern,ConvertStrategy> entry : convertMap.entrySet() ) {
            if ( entry.getKey().matcher(chNumber).matches() )
                return entry.getValue().convert(chNumber);
        }
        throw new ParseException("Can not parse "+chNumber, 0);
    }

    public static class TheTest extends TestCase {

        public void testParse() {
            try {
                //assertEquals( , ChNumber.parseString("") );
                assertEquals(8, ChineseNumber.parse("八"));
                assertEquals(79, ChineseNumber.parse("七九"));
                assertEquals(12, ChineseNumber.parse("十二"));
                assertEquals(31, ChineseNumber.parse("三十一"));
                assertEquals(503, ChineseNumber.parse("五〇三"));
                assertEquals(499, ChineseNumber.parse("四九九"));
                assertEquals(200, ChineseNumber.parse("二百"));
                assertEquals(405, ChineseNumber.parse("四百〇五"));
                assertEquals(20, ChineseNumber.parse("二十"));
                assertEquals(10, ChineseNumber.parse("十"));
                assertEquals(10, ChineseNumber.parse("一十"));
                assertEquals(100, ChineseNumber.parse("一百"));
                assertEquals(7, ChineseNumber.parse("零零七"));
                assertEquals(0, ChineseNumber.parse("〇"));
                assertEquals(231, ChineseNumber.parse("二百三十一") );
                assertEquals(630, ChineseNumber.parse("六百三十") );
            } catch (java.text.ParseException e) {
                System.out.println(e.toString());
                fail("should not throw exception");
            }
        }
    }

}

