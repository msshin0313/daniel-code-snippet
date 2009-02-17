// http://community.funstars.com/gallery/Wellweb/Western/Aihley_Allen.jpg
// http://community.funstars.com/cgi-bin/imageView.cgi?direct=Wellweb/Western&img=6

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

class ImageHarvester 
{
    protected HttpURLConnection conn;
    //FileWriter log200;  // 记录small和ok和other的情况
    FileWriter log404;  // 记录404的情况
    PrintWriter file;

	public static void main(String[] args) 
	{
        ImageHarvester session = new ImageHarvester();
        
        Properties p = new Properties();
        int start=0, end=0;
        try {
            p.load( new FileInputStream( "config.properties" ) );
            start = Integer.parseInt( p.getProperty( "start" ) );
            end   = Integer.parseInt( p.getProperty( "end" ) );
        } catch ( Exception e ) {
            System.err.println( "Can't Initialize" );
            System.exit(3);
        }
        
        try {
            //session.log200 = new FileWriter( "log200.txt", true ); // append mode
            session.log404 = new FileWriter( "log404.txt", true );
            session.file = new PrintWriter( new FileWriter( "result.txt", true ) );
        } catch ( IOException e ) {
            System.err.println( "Cannot open log file" );
            System.exit( 4 );
        }

        while ( start <= end ) {
            String filename = ""+start;
            start+=6;
            session.makeConn( filename );
            //session.getConnInfo();
            session.retrieveAndSave( filename );
            session.freeConn();
            //System.out.println( "Retrieve successfully!" );
        }

        try {
            //session.log200.close();
            session.log404.close();
            session.file.close();
        } catch ( IOException e ) {
            System.err.println( "Can not close logfile" );
            System.exit(5);
        }
	}

    private void makeConn( String filename )
    {
        String basename = "http://community.funstars.com/cgi-bin/imageView.cgi?direct=Wellweb/Western&img=";
        String urlname = basename + filename;
        URL url = null;
        try {
            url = new URL( urlname );
        } catch ( MalformedURLException e ) {
            System.err.println( "URL Format Error!" );
            System.exit(1);
        }
        try {
            conn = (HttpURLConnection)url.openConnection();
        } catch ( IOException e ) {
            System.err.println( "Error IO" );
            System.exit(2);
        }
    }

    private void freeConn()
    {
        conn.disconnect();
    }

    private void getConnInfo()
    {
        try {
            conn.connect();
            System.out.println( "Response code/message: " + conn.getResponseCode() + " / " + conn.getResponseMessage() );
            System.out.println( "Content Encoding: " + conn.getContentEncoding() );
            System.out.println( "Content Length: " + conn.getContentLength() );
            System.out.println( "Content Type: " + conn.getContentType() );
            
            int i=0;
            while ( conn.getHeaderFieldKey(i) != null ) {
                System.out.println( conn.getHeaderFieldKey(i)+": "+conn.getHeaderField(i) );
                i++;
            }
            //System.out.println( "" + conn );
        } catch ( Exception e ) {
            System.err.println( "Error" );
            System.exit(2);
        }
    }

    private void retrieveAndSave( String filename )
    {
        try {

            if ( conn.getResponseCode() == 404 ) {
                System.out.println( "404: "+filename );
                log404.write( filename+"\n" );
                return;
            } else if ( conn.getResponseCode() != 200 ) {
                System.out.println( "HTTP code is: "+conn.getResponseCode() );
                return;
            } else {
                InputStream stream = conn.getInputStream();
                StringBuffer sb = new StringBuffer();
                try {
                    int ch = stream.read();
                    while ( ch!=-1 ) {
                        sb.append( (char)ch );
                        ch = stream.read();
                    }
                } catch ( Exception e ) {
                    System.err.println( "IO error" );
                    System.exit(1);
                }
                //System.out.println( sb.toString() );

                Pattern pattern = Pattern.compile( "<a .*?&image=(.*?[.]jpg)&.*?>", Pattern.DOTALL );
                Matcher matcher = pattern.matcher( sb );
                while ( matcher.find() ) {
                    String group = matcher.group(1);
                    System.out.println( group );
                    file.println( "http://community.funstars.com/gallery/Wellweb/Western/"+group );
                }
/*
                FileOutputStream file = new FileOutputStream( filename );   // 注意：没有判断重名的情况
                int c;
                while ( (c=stream.read()) != -1 ) file.write( c );
                System.out.println( "Process "+filename );
                //System.out.println( sb.toString() );
                */
            }

        } catch ( Exception e ) {
            System.err.println( "Error: "+e.getMessage() );
            System.exit(2);
        }
    }
}
