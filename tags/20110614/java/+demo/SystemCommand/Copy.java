/**
 * @author = cobra
 * Copyright GPL
 */
import java.io.*;

class Copy {
	
	public static void main( String args[] ) {
		System.out.println( "Starting Copy..." );
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			// �����xcopy���������⣬��Ҫ�û�����
			// ��copy���������Ϊ��command shell���������.exe
			p = rt.exec( "xcopy c:\\autoexec.bat c:\\a.txt" );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
