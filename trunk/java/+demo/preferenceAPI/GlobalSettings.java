/*
 * 来自MMViewer的一个类的demo，用来演示preference API
 * preference主要用来存储程序设置
 * 这个类还用了singleton pattern
 */
import java.util.prefs.*;

class GlobalSettings {
	public java.io.File srcDir;
	public java.io.File destDir;
	
	// 这一句生成了一个preference实例，windows中默认存在user registry中
	private Preferences prefs = Preferences.userNodeForPackage( this.getClass() );
	
	private void load() {
		String tempStr;
		// .get()从注册表中取出数据
		tempStr = prefs.get("srcDir",null);
		srcDir = tempStr==null ? null : new java.io.File( tempStr );
		tempStr = prefs.get("destDir",null);
		destDir = tempStr==null ? null : new java.io.File( tempStr );
	}
	
	public void saveDir() {
		// .put()将数据存入注册表
		if ( srcDir != null ) prefs.put( "srcDir", srcDir.toString() );
		if ( destDir != null ) prefs.put( "destDir", destDir.toString() );
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