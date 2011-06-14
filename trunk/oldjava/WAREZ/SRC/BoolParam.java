/***************************************************************
 * ProgramID:	JAPP02-07.
 * Project:		warez: a collection	of useful utilities.
 * Version:		1.0.
 * Created:		2002-11-2.
 * LastUpdated:	2002-11-2.
 * Developer:	cobra.
 * Description:	输入一个boolean类型
 * Copyright:	GPL.
****************************************************************/

class BoolParam implements Param
{
	private Boolean bool;

	public String getPrompt()
	{
		return "[y/n]";
	}

	public void setString( String input ) throws Exception
	{
		if ( input==null || input.length()==0 )
			throw new Exception( "Give me something." );
		char ch = Character.toLowerCase( input.charAt( 0 ) );
		if ( ch=='y' )
			bool = new Boolean( true );
		else if ( ch=='n' )
			bool = new Boolean( false );
		else
			throw new Exception( "only 'y' or 'n' is acceptable!" );
	}

	public Object getParam()
	{
		return bool;
	}
}