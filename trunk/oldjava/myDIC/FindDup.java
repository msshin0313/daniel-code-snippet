package apps.myDIC;

import java.io.*;
import java.util.*;

class FindDup
{
	public static void main( String args[] )
	{
		if ( args.length == 0 ) {
			System.out.println ( "Usage: FindDup [file1] [file2] ..." );
			return;
		}
		
		HashSet set = new HashSet();
		String aline=null, word=null;
		for ( int index=0; index<args.length; index++ ) {
			try {
				BufferedReader in = new BufferedReader( new FileReader(args[index]) );
				while ( (aline=in.readLine()) != null ) {
					word = aline.substring( 0, aline.indexOf((int)'\t') );
					if ( ! set.add( word ) )
						System.out.println( "Duplicate word: '"+word+"' in "+args[index] );
				}
			} catch ( IndexOutOfBoundsException e ) {
				System.out.println ( "Word: '"+word+"' does not have meaning" );
			} catch ( IOException e ) {
				System.err.println( e );
			} // end of try
		} // end of for
	} // end of main()
} // end of class