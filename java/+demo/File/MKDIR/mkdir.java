import java.io.*;

class mkdir 
{
	public static void main(String[] args) 
	{
        try {
		    File dir = new File( "level1\\level2\\" );
            dir.mkdirs();
            PrintWriter out = new PrintWriter( new FileWriter( "level1\\level2\\hello.txt" ) );
            out.println( "Hello, World" );
            out.close();
        } catch ( IOException e ) {
            System.out.println( e );
        }
	}
}
