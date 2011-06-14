/***************************************************************
 * ProgramID:   JAPP01-05.
 * Project:     MyTime2.
 * Version:     beta 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-15.
 * Developer:   Cobra.
 * Description: ������ʾһ��ʱ�䣬����һ�죬һ�ܵȵȡ����ݴ�EntrySeries����ȡ�������л���
 * Copyright:   GPL.
****************************************************************/
import java.util.*;
import java.text.SimpleDateFormat;

class Segment
{
    public int typeSum[];
    public int etypeSum[];  // ����Ч������
    public int catSum[];
    public int ecatSum[];   // ����Ч������
    public Vector events;
    protected EntrySeries entry;    // ��¼һ��EntrySeries��ָ�룬��������generate����
    protected Date startDate;   // segment��ʼ��ʱ�䣬������������ṩ��Ϣ
    protected Date endDate;     // segment������ʱ��

    public Date getStartDate() { return startDate; }
    public Date getEndDate()   { return endDate; }

    /**Pre   :
     * Post  :
     * Usage :
    ** Param : */
    public Segment( EntrySeries e )
    {
        assert e!=null;
        assert e.size()>0;
        typeSum = new int[ EventType.typeTable.size() ];
        for ( int i=0; i<typeSum.length; i++ )
            typeSum[i] = 0;
        catSum  = new int[ EventType.categoryTable.size() ];
        for ( int i=0; i<catSum.length; i++ )
            catSum[i] = 0;
        etypeSum = new int[ EventType.typeTable.size() ];
        for ( int i=0; i<etypeSum.length; i++ )
            etypeSum[i] = 0;
        ecatSum  = new int[ EventType.categoryTable.size() ];
        for ( int i=0; i<ecatSum.length; i++ )
            ecatSum[i] = 0;
        events  = new Vector();
        entry = e;  // ע�⣬�����ø�ֵ
    }

    /**Pre   :day�����Ǵ�һ����賿��ʼ
     * Post  :ͳ����һ�����Ϣ
     * Usage :
    ** Param : */
    public void generate( Date day )
    {
        generate( day, 1 );
    }

    /**Pre   :day�����Ǵ�һ����賿��ʼ
     * Post  :ͳ�ƽ���ȥn�����Ϣ
     * Usage :
    ** Param : */
    public void generate( Date day, int n )
    {
        assert day!=null;
        SimpleDateFormat df = new SimpleDateFormat( "HHmmssSSSS" );
        if ( !df.format(day).equals("0000000000") )    // ���Ǵ��賿��ʼ
            throw new IllegalArgumentException( "Segment.generate(Date) should start at 00:00:00AM" );
        Date dayend = new Date( day.getTime()+n*86400000 );   // �ڶ����賿
        generate( day, dayend );
    }

    /**Pre   :start, end���ܴ���ͳһʱ������
     * Post  :���ɴ�start��end֮��Ļ�����Ϣ
     * Usage :
    ** Param : */
    public void generate( Date start, Date end )
    {
        assert start!=null && end!=null;
        assert start.before( end );
        startDate = start;
        endDate   = end;

        // ��ʼ������ʼgenerate�Ķ�λ��ʹstart�ܱ��ҵ���λ����
        int index;
        Entry currentry = null;
        for ( index=0; index<entry.size(); index++ ) { // ��λ
            currentry = (Entry)entry.elementAt(index);
            if ( start.before(currentry.time) ) {
                if ( end.before(currentry.time) ) {
                    // ��UnsupportedOperationException����Ϊ�������Դ���ֻ��û���ٴ������
                    throw new UnsupportedOperationException( "Segment.generate: Event 'start' 'end' can not be in the same duration" );
                } else break;
            }
        }   // index�л��������¼��λ��
        //currentry.print();  // debug
        if ( index == entry.size() )    // ��ʼ��ʱ�䳬��entry�ķ�Χ
            return;
        EventEntry event;

        // ����start���ܰ�һ��������event�ֳ����룬����ҪԤ�ȴ���
        // һ����������жϵ���Ϣ�����ֿ��������е���һ��������
        // ������õķ����ǣ���һ��ʱ��ϳ���������������ʱ����ж�ʱ���Դ��ڸö�ʱ�䣬��������д���
        long last;
        if ( index != 0 ) {
            last = ((Entry)entry.elementAt(index-1)).time.getTime();
        } else last = 0;    // ��index==0��last��ʵ��û��ʲô����
        long next = currentry.time.getTime();
        long curr = start.getTime();
        // ����һ���µ�ʱ��㣬�����ָ����event
        Entry startentry = new Entry();
        startentry.time = start;
        // �ж��жϵ���ϢӦ�ý���˭�����������鵽��һ��
        if ( index==0 || (next-curr)>=(curr-last) ) {
            event = new EventEntry( startentry, currentry );
        } else { // index!=0 && (next-curr)<(curr-last)
            // ����˵�����жϵ���Ϣ������һ�δ���
            Entry en = (Entry)currentry.clone();
            en.breakNum = 0;
            en.breakTime = 0;
            event = new EventEntry( startentry, en );
        }
        if ( event.breakTime > event.duration )
            // ��UnsupportedOperationException����Ϊ�������Դ���ֻ��û���ٴ������
            throw new UnsupportedOperationException( "Segment.generate: start BreakTime > Duration" );
        summarize( event );

        // ��ʼ��������������¼�
        index++;
        Entry lastentry = currentry;
        // ��endͬcurrentry���ʱ������currentry after end��ȡ��end��ʱ�䡣����������.before������!.after
        while ( index<entry.size() && (currentry=(Entry)entry.elementAt(index)).time.before(end) ) {
            event = new EventEntry( lastentry, currentry );
            summarize( event );
            lastentry = currentry;
            index++;
        }

        // ��β����
        if ( index==entry.size() ) { // ȫ��������
            endDate = currentry.time;   // ���ý���ʱ��Ϊ���һ��entry��ʱ��
            return;
        }
        last = lastentry.time.getTime();
        next = currentry.time.getTime();
        curr = end.getTime();
        Entry endentry = (Entry)currentry.clone();
        endentry.time = end;
        // ������ж���Ϣ�鵽��һ��
        if ( curr-last <= next-curr ) {
            endentry.breakNum = 0;
            endentry.breakTime= 0;
        }
        event = new EventEntry( lastentry, endentry );
        if ( event.breakTime > event.duration )
            // ��UnsupportedOperationException����Ϊ�������Դ���ֻ��û���ٴ������
            throw new UnsupportedOperationException( "Segment.generate: end BreakTime > Duration" );
        summarize( event );
    }

    /**Pre   :
     * Post  :��һ���¼�ͳ����һ��Segment��
     * Usage :
    ** Param : */
    private void summarize( EventEntry e )
    {
        assert e != null;
        int typeid = e.type.getTypeID();
        int catid  = e.type.getCategoryID();
        typeSum [ typeid ] += e.duration;
        etypeSum[ typeid ] += (e.duration-e.breakTime)*e.efficiency/100.0;
        catSum  [ catid ]  += e.duration;
        ecatSum [ catid ]  += (e.duration-e.breakTime)*e.efficiency/100.0;
        events.addElement( e );
    }
}