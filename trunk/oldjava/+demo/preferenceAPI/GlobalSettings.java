/*
 * ����MMViewer��һ�����demo��������ʾpreference API
 * preference��Ҫ�����洢��������
 * ����໹����singleton pattern
 */
import java.util.prefs.*;

class GlobalSettings {
	public java.io.File srcDir;
	public java.io.File destDir;
	
	// ��һ��������һ��preferenceʵ����windows��Ĭ�ϴ���user registry��
	private Preferences prefs = Preferences.userNodeForPackage( this.getClass() );
	
	private void load() {
		String tempStr;
		// .get()��ע�����ȡ������
		tempStr = prefs.get("srcDir",null);
		srcDir = tempStr==null ? null : new java.io.File( tempStr );
		tempStr = prefs.get("destDir",null);
		destDir = tempStr==null ? null : new java.io.File( tempStr );
	}
	
	public void saveDir() {
		// .put()�����ݴ���ע���
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