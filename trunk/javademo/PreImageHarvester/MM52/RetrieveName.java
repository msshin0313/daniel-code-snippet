import java.util.regex.*;
import java.io.*;

class RetrieveName
{
	public static void main(String[] args) 
	{
        StringBuffer sb = new StringBuffer();
        try {
            FileReader in = new FileReader( "other.txt" );
            int ch;
            while ( (ch=in.read())!=-1 ) sb.append( (char) ch );
        } catch ( IOException e ) {
            System.out.println( e );
        }

        Pattern pattern = Pattern.compile( "value=\"(.*?)/", Pattern.DOTALL );  // Reluctant Mode就是在后面加?，JavaAPI doc中的X??其实应该是X+?
        Matcher matcher = pattern.matcher( sb );
        while ( matcher.find() ) {
            String name = matcher.group(1);
            System.out.println( name );
        }
	}
}
