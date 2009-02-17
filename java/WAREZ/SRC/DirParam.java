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

class DirParam implements Param
{
	private File dir;

	public String getPrompt()
	{
		return "Input Full Directory Name: ";
	}

	public void setString( String input ) throws Exception
	{
		if ( input==null || input.length()==0 )
			throw new Exception( "Give me something." );
		dir = new File( input );
		if ( ! dir.exists() )
			throw new Exception( "Directory does not exist!" );
		if ( ! dir.isDirectory() )
			throw new Exception( "Please give me a Directory, not a file!" );
		if ( ! dir.canWrite() )
			throw new Exception( "Please give me a writable directory, otherwise the program will not proceed!" );
	}

	public Object getParam()
	{
		return dir;
	}
}
