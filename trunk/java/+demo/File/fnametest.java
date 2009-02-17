import java.io.*;

class fnametest 
{
	public static void main(String[] args) 
	{
		File ofile = new File( "c:\\temp\\dir1\\dir2\\file1.txt" );
		File file = new File( "file2.txt" );
		System.out.println( ofile.renameTo( file ) );
		System.out.println( file.getName() );
		System.out.println( file.getPath() );
		//System.out.println( File.pathSeparatorChar );
		System.out.println( file.getAbsolutePath() );
		//try { System.out.println( file.getCanonicalPath() ); } catch (IOException e) {}
		System.out.println( file.getParent() );
		//System.out.println( file.toString() );
		//System.out.println();
		//System.out.println("Hello World!");
	}
}
