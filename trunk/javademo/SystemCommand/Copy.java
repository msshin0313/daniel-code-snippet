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
			// 下面的xcopy参数有问题，需要用户交互
			// 用copy命令还出错，因为是command shell的命令，不是.exe
			p = rt.exec( "xcopy c:\\autoexec.bat c:\\a.txt" );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
