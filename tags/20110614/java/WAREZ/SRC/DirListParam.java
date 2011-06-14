/***************************************************************
 * ProgramID:	JAPP02-05.
 * Project:		warez: a collection	of useful utilities.
 * Version:		1.0.
 * Created:		2002-11-2.
 * LastUpdated:	2002-11-2.
 * Developer:	cobra.
 * Description:	用来返回一个可写的Dir的类
 * Copyright:	GPL.
****************************************************************/
import java.io.*;

class DirListParam extends DirParam
{
	public void setString( String input ) throws Exception
	{
		super.setString( input );
		File dir = (File) getParam();
		File[] files = dir.listFiles();
		System.out.println( "You choose: " + dir.getAbsolutePath() );
		System.out.println( "which contains: " );
		for ( int i=0; i<10 && i<files.length; i++ ) {
			System.out.println( "\t" + files[i].getName() );
		}
		System.out.println( "\t......" );
	}
}