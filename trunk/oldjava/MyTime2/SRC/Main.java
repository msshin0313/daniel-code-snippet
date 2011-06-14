/***************************************************************
 * ProgramID:   JAPP01-01.
 * Project:     MyTime2.
 * Version:     alpha 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-17
 * Developer:   Cobra.
 * Description: ������.
 * Copyright:   GPL.
****************************************************************/
import java.util.*;
import java.io.*;
import java.text.*;

class  Main
{
	public static void main(String[] args) 
	{
        Properties config = new Properties();
        try {
            config.load( new FileInputStream( "config.properties" ) );
        } catch (Exception e) {
            System.err.println( "Read File Error" );
            System.exit(3);
        }

        File file = new File( config.getProperty("datafile") );
        EntrySeries entries = new EntrySeries();
        try {
            entries.readEntry( file );
            //System.out.println( "Process Input data Successfully" );
        } catch ( IOException e ) {
            System.out.println( "Error occurs: "+e.getMessage() );
            System.exit(1);
        }
        Segment segment = new Segment( entries );
        /*
        for ( int i=0; i<entries.size(); i++ ) {
            Entry e = (Entry)entries.elementAt(i);
            e.print();
        }
        */

        // ѡ����ģʽ
        String workMode = config.getProperty( "workMode" );
        if ( workMode==null || workMode.equals("") ) {
            throw new RuntimeException( "config.properties: workMode is not defined" );
        } else if ( workMode.equals("latestWeek") ) {   // ���������һ�����������ڵ�ʱ��

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add( Calendar.DATE, -7 );
            calendar.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );
            calendar.set( Calendar.HOUR_OF_DAY, 0 );
            calendar.set( Calendar.MINUTE, 0 );
            calendar.set( Calendar.SECOND, 0 );
            calendar.set( Calendar.MILLISECOND, 0 );
            Date start = calendar.getTime();
            calendar.add( Calendar.DATE, 7 );   // ���7��֮���Date
            Date end = calendar.getTime();
            if ( end.before( ((Entry)entries.elementAt(0)).time ) ) {
                Main.printError( "latestWeek mode requirs at least one whole week" );
                System.exit( 5 );
            }
            segment.generate( start, 7 );
            printSegment( segment );

        } else if ( workMode.equals("currWeek") ) {   // ���㵱ǰ���������ڵ�ʱ��

            Date lastTime = ((Entry)entries.lastElement()).time; // ȡ������һ����¼��ʱ��
            lastTime = (Date)lastTime.clone();  // ��Ϊһ��copy����Ϊ����Ҫ�޸�
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime( lastTime );
            calendar.set( Calendar.HOUR_OF_DAY, 0 );
            calendar.set( Calendar.MINUTE, 0 );
            calendar.set( Calendar.SECOND, 0 );
            calendar.set( Calendar.MILLISECOND, 0 );
            int today = calendar.get( Calendar.DAY_OF_WEEK );
            if ( today == Calendar.MONDAY ) {
                Main.printError( "currWeek mode requires at least one day" );
                System.exit( 5 );
            }
            if ( today == Calendar.SUNDAY )
                today = 8; // MONDAY==2, SUNDAYԭΪ1
            calendar.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );
            Date start = calendar.getTime();
            segment.generate( start, today-2 );
            printSegment( segment );

        } else if ( workMode.equals("Process") ) {  // �������һ�յ�ʱ��

            Date lastTime = ((Entry)entries.lastElement()).time; // ȡ������һ����¼��ʱ��
            lastTime = (Date)lastTime.clone();  // ��Ϊһ��copy����Ϊ����Ҫ�޸�
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime( lastTime );
            calendar.set( Calendar.HOUR_OF_DAY, 0 );
            calendar.set( Calendar.MINUTE, 0 );
            calendar.set( Calendar.SECOND, 0 );
            calendar.set( Calendar.MILLISECOND, 0 );
            Date start = calendar.getTime();
            segment.generate( start, 1 );
            printSegment( segment );

        } else if ( workMode.equals("manual") ) {   // �ֶ����ÿ�ʼ�ͽ���ʱ��

            Date start=null, end=null;
            SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
            try {
                start = df.parse( config.getProperty("start") );
                end   = df.parse( config.getProperty("end") );
            } catch ( ParseException e ) {
                System.err.println( "Can not parse time info" );
                System.exit(4);
            }
            segment.generate( start, end );
            printSegment( segment );

        } else {
            throw new RuntimeException( "config.properties: workMode format error" );
        }
	}

    // �����ṩһ��ͳһ��error������棬�����Ժ���GUI��ֲ
    public static void printError( String msg )
    {
        System.err.println( "General Error: "+msg );
    }
    
    public static void printSegment( Segment seg )
    {
        int totalTime = 0;
        for ( int i=0; i<EventType.categoryTable.size(); i++ ) {
            totalTime += seg.catSum[i];
        }
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        System.out.println( "Date Segment: "
                +df.format(seg.getStartDate())+" ~~ "
                +df.format(seg.getEndDate()) );
        
        int totalday = totalTime/60/24;
        if ( totalday == 0 ) totalday = 1;  // ����ʱ��������һ��
        System.out.println( "total day: "+totalday );

        System.out.println();
        System.out.println( "Type:" );
        for ( int i=0; i<EventType.typeTable.size(); i++ ) {
            System.out.println( (String)EventType.typeTable.elementAt(i)
                + "\t" + formatMinute(seg.typeSum[i])
                + "\t" + formatMinute(seg.etypeSum[i])
                + "\t" + formatMinute(seg.typeSum[i]/totalday) );
        }

        System.out.println();
        System.out.println( "Category: " );
        for ( int i=0; i<EventType.categoryTable.size(); i++ ) {
            System.out.println( (String)EventType.categoryTable.elementAt(i)
                + "\t" + formatMinute(seg.catSum[i])
                + "\t" + formatMinute(seg.ecatSum[i])
                + "\t" + formatMinute(seg.catSum[i]/totalday) );
        }

        System.out.println();
        System.out.println( "total time: "+formatMinute(totalTime) );
    }

    public static final String formatMinute( int min )
    {
        return min/60 + ":" +min%60;
    }
}
