package magicstudio.mmviewer;

/*
 * @(#)magicstudio.mmviewer.MMViewer.java 1.0 03/06/10
 */
import java.util.prefs.*;

class GlobalSettings {
	public java.io.File srcDir;
	public java.io.File destDir;
	public int unitIncrement;	// unit of UP/DOWN/LEFT/RIGHT increment
	public boolean fullScreen;
	public boolean includeSubDir;
	
	private Preferences prefs = Preferences.userNodeForPackage( this.getClass() );
	private void load() {
		// use java.util.prefs to setup initial environments
		String tempStr;
		tempStr = prefs.get("srcDir",null);
		srcDir = tempStr==null ? null : new java.io.File( tempStr );
		tempStr = prefs.get("destDir",null);
		destDir = tempStr==null ? null : new java.io.File( tempStr );
		// = prefs.get("",);
		unitIncrement = prefs.getInt("unitIncrement",30);
		fullScreen = prefs.getBoolean("fullScreen",true);
		includeSubDir = prefs.getBoolean("includeSubDir",true);
	}
	
	public void saveDir() {
		if ( srcDir != null ) prefs.put( "srcDir", srcDir.toString() );
		if ( destDir != null ) prefs.put( "destDir", destDir.toString() );
	}
	
	public void saveGeneral() {
		prefs.putInt("unitIncrement",unitIncrement);
		prefs.putBoolean("fullScreen",fullScreen);
		prefs.putBoolean("includeSubDir",includeSubDir);
	}
	
	// singleton implementation
	private GlobalSettings() {}
	private static GlobalSettings gs = null;
	public static GlobalSettings getSettings() {
		if ( gs == null ) {
			gs = new GlobalSettings();
			gs.load();
		}
		return gs;
	}
}