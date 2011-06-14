// Retrieve images from 'www.bestmm.com'
// 规则是：http://www.bestmm.com/001/pic/01.jpg   001-010   01.jpg-50.jpg
//         http://www.bestmm.com/e001/pic/01.jpg  e001-e008 01.jpg-50.jpg

import java.net.*;
import java.io.*;
import java.util.*;

class ImageHarvester 
{
    protected HttpURLConnection conn;
    //FileWriter log200;  // 记录small和ok和other的情况
    FileWriter log404;  // 记录404的情况

	public static void main(String[] args) 
	{
        ImageHarvester session = new ImageHarvester();
                
        try {
            //session.log200 = new FileWriter( "log200.txt", true ); // append mode
            session.log404 = new FileWriter( "log404.txt", true );
        } catch ( IOException e ) {
            System.err.println( "Cannot open log file" );
            System.exit( 4 );
        }

        int tag1=6;
        int tag2=1;

        while ( tag1<=10 )
        {
            while ( tag2<=50 )
            {
                String filename1 = (tag1<10)?"00"+tag1:"0"+tag1;
                String filename2 = (tag2<10)?"0"+tag2:""+tag2;
                session.makeConn( filename1, filename2 );
                //session.getConnInfo();
                session.retrieveAndSave( filename1, filename2 );
                session.freeConn();
                //System.out.println( "Retrieve successfully!" );
                tag2++;
            }
            tag1++;
            tag2=1;
            //System.err.println("tag1: "+tag1);
        }

        try {
            //session.log200.close();
            session.log404.close();
        } catch ( IOException e ) {
            System.err.println( "Can not close logfile" );
            System.exit(5);
        }
	}

    private void makeConn( String filename1, String filename2 )
    {
        String basename = "http://www.bestmm.com/";
        String urlname = basename+filename1+"/pic/"+filename2+".jpg";
        //System.err.println(urlname);
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

    private void retrieveAndSave( String filename1, String filename2 )
    {
        String filename = "j"+filename1+"-"+filename2+".jpg";
        try {

            if ( conn.getResponseCode() == 404 ) {
                System.out.println( "404: "+filename );
                log404.write( filename+"\n" );
                return;
            } else if ( conn.getResponseCode() != 200 ) {
                System.out.println( "HTTP code is not 404/200" );
                log404.write( "xx: "+filename+"\n" );
                return;
            } else {
                InputStream stream = conn.getInputStream();
                FileOutputStream file = new FileOutputStream( filename );   // 注意：没有判断重名的情况
                int c;
                while ( (c=stream.read()) != -1 ) file.write( c );
                System.out.println( "Process "+filename );
                //log200.write( "ok: "+filename+"\n" );
                //System.out.println( sb.toString() );
            }

        } catch ( Exception e ) {
            System.err.println( "Error: "+e.getMessage() );
            System.exit(2);
        }
    }
}
