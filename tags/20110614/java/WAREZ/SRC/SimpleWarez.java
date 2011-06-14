/***************************************************************
 * ProgramID:	JAPP02-03.
 * Project:		warez: a collection	of useful utilities.
 * Version:		1.0.
 * Created:		2002-10-26.
 * LastUpdated:	2002-11-2.
 * Developer:	cobra.
 * Description:	最简单的Warez子类，主要用于测试
 * Copyright:	GPL.
****************************************************************/
import java.io.*;

class SimpleWarez extends Warez
{
	public String getName()
	{
		return ( "Simple Warez" );
	}

	public String getDescription()
	{
		return ( "Used for test." );
	}

	protected void registerParams()
	{
		params.add( new DirParam() );
	}

	public void execute()
	{
		File dir = (File) ( (Param)params.get(0) ).getParam();
		System.out.println( dir.toString() );
	}
}