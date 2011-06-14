/**
 * @author = cobra
 * Copyright GPL
 * 从jinan.txt中读取数据格式化，生成想要的文件
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
		// 下面的表达式为：以K或没有K开头，接着是若干数字，然后是“路”字。
		// 注意()表示group，以便提取其中的信息
		Pattern p = Pattern.compile( "^([Kk]?\\d+路).*$" );
		Matcher m;
		boolean flag = true;
		try {
			while( true ) {
				line = in.readLine();
				if ( line == null ) break;
				else if ( line.equals("") ) continue;
				
				m = p.matcher( line );
				if ( flag != m.find() )	// 这里必须先用find()触发匹配过程，才能开始使用
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
