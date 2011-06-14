package apps.lineCount;

import java.io.*;
import java.util.regex.*;

class lineCount
{
	public static void main( String args[] )
	{
		if ( args.length != 1 ) {
			System.out.println( "Usage: lineCount [dir]" );
			return;
		}
		
		int totalLine = 0;
		try {
			File dir = new File ( args[0] );
			if ( ! dir.isDirectory() )
				throw new IOException( args[0]+" is not a directory!" );
				
			File[] files = dir.listFiles();
			for ( int index=0; index<files.length; index++ ) {
				File afile = files[index];
				if ( afile.isDirectory() ) continue;
				String fname = afile.getName();
				if ( fname.matches(".*\\.java") ) {
					LineNumberReader in = new LineNumberReader( new FileReader(afile) );
					while ( in.readLine() != null );	// read to the end of the file
					totalLine += in.getLineNumber();
					in.close();
				}
			}
			System.out.println( "totalLine: "+totalLine );
		} catch ( IOException e ) {
			System.out.println( e );
		}
	}

}