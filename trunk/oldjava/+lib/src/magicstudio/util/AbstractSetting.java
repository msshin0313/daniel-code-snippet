package magicstudio.util;

/**
 * Date: 2004-1-13
 * Time: 14:37:47
 * Desc: 
 * Progress: Defining interface
 */
public abstract class AbstractSetting {

    public static void initFromPrefs() {

    }

    abstract protected void addDefaultSettings();

    protected final void add(String key, Object value) {

    }

    // return all the settings' keys
    public String[] keys() {
        return null;
    }

    public void saveToPrefs() {

    }

    public void setString(String key, String value) {

    }

    public void setInteger(String key, int value) {

    }

    public void setBoolean(String key, boolean value) {

    }

    public String getString(String key) {
        return null;
    }

    public int getInteger(String key) {
        return 0;
    }

    public boolean getBoolean(String key) {
        return false;
    }
}
