/**
 * @author = cobra
 * Copyright GPL
 * ��jinan.txt�ж�ȡ���ݸ�ʽ����������Ҫ���ļ�
 * 2003.5.2
 */
 
import java.util.regex.*;
import java.io.*;

class Jinan {
	
	public static void main( String args[] ) {
		//System.out.println( "Starting Jinan..." );
		LineNumberReader in = null;
		BufferedWriter out = null;
		try {
			in = new LineNumberReader( new FileReader( "Jinan.txt" ) );
			out = new BufferedWriter( new FileWriter( "format.txt" ) );
		} catch( IOException e ) {
			System.out.println( e );
			System.exit( -1 );
		}
		
		String line;
		// ����ı��ʽΪ����K��û��K��ͷ���������������֣�Ȼ���ǡ�·���֡�
		// ע��()��ʾgroup���Ա���ȡ���е���Ϣ
		Pattern p = Pattern.compile( "^([Kk]?\\d+·).*$" );
		Matcher m;
		boolean flag = true;
		try {
			while( true ) {
				line = in.readLine();
				if ( line == null ) break;
				else if ( line.equals("") ) continue;
				
				m = p.matcher( line );
				if ( flag != m.find() )	// �����������find()����ƥ����̣����ܿ�ʼʹ��
					throw new RuntimeException( "File format error: "+in.getLineNumber() );
				if ( flag == true ) {
					out.write( m.group(1) );	// group(0) is the entire string, group(1) is the right one
					out.write( " " );
					flag = false;
				} else {
					out.write( line );
					out.newLine();
					flag = true;
				}
			}
			in.close();
			out.close();
		} catch ( IOException e ) {
			System.out.println( e );
			System.exit( -1 );
		}
	}

}
