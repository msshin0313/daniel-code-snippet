/**
 * User: Dan
 * Date: 2004-1-7
 * Time: 13:20:31
 */
package magicstudio.mmviewer;

import java.util.Map;
import java.util.HashMap;

class Setting {
    private static Setting _instance;

    // default initialization
    public static void initialize() {
        assert _instance == null;
        _instance = new Setting();
    }

    // initialization using command line
    public static void initialize(String[] args) throws WrongParamsException {
        assert args != null;
        initialize();
        WrongParamsException wp = new WrongParamsException();
        for ( int index=0; index<args.length; index++) {
            try {
                modifySetting( args[index] );
            } catch (WrongParamsException e) {
                wp.add(e);
            }
        }
        if (!wp.isEmpty()) {
            _instance = null;   // if some params are wrong, then no initialization at all
            throw wp;
        }
    }

    // input 'setting' should be like,e.g., 'boolFullScreen=true'
    private static void modifySetting(String setting) throws WrongParamsException {
        // todo: here
        setting.split("=", 2);
    }

    /// Above are static fields/methods, below are not.

    private Map _setting;

    private Setting() {
        _setting = new HashMap();
        _setting.put("intUnitIncrement", new Integer(5));  // pixel unit of UP/DOWN/LEFT/RIGHT increment
        _setting.put("boolIncludeSubDir", Boolean.TRUE);
        _setting.put("boolFullScreen", Boolean.TRUE);
    }

    public synchronized Object get(String key) {
        return new Object();
    }

    public synchronized int getInt(String key) {
        return 0;
    }

    public synchronized boolean getBoolean(String key) {
        return true;
    }
}

