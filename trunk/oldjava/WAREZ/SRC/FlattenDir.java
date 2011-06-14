/***************************************************************
 * ProgramID:	JAPP02-06.
 * Project:		warez: a collection	of useful utilities.
 * Version:		1.0.
 * Created:		2002-11-2.
 * LastUpdated:	2002-11-2.
 * Developer:	cobra.
 * Description:	��ĳĿ¼�µ�������Ŀ¼�е��ļ�ת�Ƶ���Ŀ¼�¡�
				ע�⣺��ǰ�汾��û�д���������ͻ�����⡣
 * Copyright:	GPL.
****************************************************************/
import java.io.*;

class FlattenDir extends Warez
{
	public String getName()
	{
		return ( "Flatten Directory" );
	}

	public String getDescription()
	{
		return ( "Move files from subdirectory to the give directory.\n"
				+"Files' name may be changed!" );
	}

	protected void registerParams()
	{
		params.add( new DirListParam() );
		params.add( new MyBoolParam() );
	}

	public void execute()
	{
		File dir = (File) ( (Param)params.get(0) ).getParam();
		Boolean bool = (Boolean) ( (Param)params.get(1) ).getParam();
		if ( bool.booleanValue() == true )
			flatten( dir );
	}

	protected void flatten( File dir )
	{
		assert dir.isDirectory();
		File[] files = dir.listFiles();
		for ( int i=0; i<files.length; i++ ) {
			File subfile = files[i];
			if ( subfile.isFile() ) {
				//File temp = new File( f.getParent()+'_'+f.getName() );
				//f.renameTo( temp );
				continue;
			} else {
				//if ( f.getName().startsWith( "_" )
				//	throw RuntimeException( "Please rename: "+f.getAbsolutePath() );
				flatten( subfile );
				File[] subfiles = subfile.listFiles();
				for ( int j=0; j<subfiles.length; j++ ) {
					File f = subfiles[j];
					File temp = new File( subfile.getParent()+'\\'+subfile.getName()+'_'+f.getName() );
					if ( f.renameTo( temp ) == false ) {
						throw new RuntimeException( "Cannot rename: "+f.toString() );
					} else {
						System.out.println( f.toString()+". Rename OK!" );
					}
				}
				if ( subfile.delete() == false ) {
					throw new RuntimeException( "Cannot delete: "+subfile.toString() );
				}
			}	// end of else
		}	// ind of for
	}

	class MyBoolParam extends BoolParam
	{
		public String getPrompt() {
			return ( "The operation is not revertable. Are you sure? [y/n]" );
		}
	}
}