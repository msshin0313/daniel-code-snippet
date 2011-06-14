/**
 * @author = cobra
 * Copyright GPL
 * Usage: list system properties in alphabit order
 */
import java.util.*;

class ListSysProperties {
	
	public static void main( String args[] ) {
		if ( args.length != 0 ) {
			for ( int i=0; i<args.length; i++ ) {
				print( args[i], System.getProperty( args[i] ) );
			}
		} else {
			Properties prop = System.getProperties();
			Object[] keyArray = prop.keySet().toArray();
			Arrays.sort( keyArray );
			for ( int i=0; i<keyArray.length; i++ ) {
				print( (String)keyArray[i], prop.getProperty( (String)keyArray[i] ) );
			}
		}
	}
	
	private static void print( String key, String value ) {
		System.out.println( "* " + key + ": " + value );
	}
}
