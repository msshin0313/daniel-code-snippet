import java.io.*;

class FileTest 
{
	public static void printTree( File path, int depth )
	{
		for ( int j=0; j<depth; j++ )
		{
			System.out.print("--");
		}
		System.out.println( path.getName() );

		File[] files = path.listFiles();
		for ( int i=0; i<files.length; i++ )
		{
			if ( files[i].isDirectory() )
			{
				printTree( files[i], depth+1 );
			}
			else
			{
				for ( int j=0; j<depth; j++ )
				{
					System.out.print("--");
				}
				System.out.println( files[i].getName() );
			}
		}
	}

	public static void main(String[] args) 
	{
		if ( args.length != 1 )
		{
			System.out.println( "You should specify exactly 1 path name" );
			return;
		}
		String pathname = args[0];
		File path = new File( pathname );
		printTree( path, 0 );
	}
}
