/***************************************************************
 * ProgramID:   JAPP01-05.
 * Project:     MyTime2.
 * Version:     beta 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-15.
 * Developer:   Cobra.
 * Description: 用来表示一段时间，比如一天，一周等等。数据从EntrySeries中提取，并进行汇总
 * Copyright:   GPL.
****************************************************************/
import java.util.*;
import java.text.SimpleDateFormat;

class Segment
{
    public int typeSum[];
    public int etypeSum[];  // 考虑效率因素
    public int catSum[];
    public int ecatSum[];   // 考虑效率因素
    public Vector events;
    protected EntrySeries entry;    // 记录一个EntrySeries的指针，用来从中generate数据
    protected Date startDate;   // segment开始的时间，用于向调用者提供信息
    protected Date endDate;     // segment结束的时间

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
        entry = e;  // 注意，是引用赋值
    }

    /**Pre   :day必须是从一天的凌晨开始
     * Post  :统计这一天的信息
     * Usage :
    ** Param : */
    public void generate( Date day )
    {
        generate( day, 1 );
    }

    /**Pre   :day必须是从一天的凌晨开始
     * Post  :统计接下去n天的信息
     * Usage :
    ** Param : */
    public void generate( Date day, int n )
    {
        assert day!=null;
        SimpleDateFormat df = new SimpleDateFormat( "HHmmssSSSS" );
        if ( !df.format(day).equals("0000000000") )    // 不是从凌晨开始
            throw new IllegalArgumentException( "Segment.generate(Date) should start at 00:00:00AM" );
        Date dayend = new Date( day.getTime()+n*86400000 );   // 第二天凌晨
        generate( day, dayend );
    }

    /**Pre   :start, end不能处在统一时间间隔内
     * Post  :生成从start到end之间的汇总信息
     * Usage :
    ** Param : */
    public void generate( Date start, Date end )
    {
        assert start!=null && end!=null;
        assert start.before( end );
        startDate = start;
        endDate   = end;

        // 初始化及开始generate的定位，使start总比找到的位置早
        int index;
        Entry currentry = null;
        for ( index=0; index<entry.size(); index++ ) { // 定位
            currentry = (Entry)entry.elementAt(index);
            if ( start.before(currentry.time) ) {
                if ( end.before(currentry.time) ) {
                    // 用UnsupportedOperationException是因为本来可以处理，只是没有再处理而已
                    throw new UnsupportedOperationException( "Segment.generate: Event 'start' 'end' can not be in the same duration" );
                } else break;
            }
        }   // index中获得下条记录的位置
        //currentry.print();  // debug
        if ( index == entry.size() )    // 开始的时间超出entry的范围
            return;
        EventEntry event;

        // 由于start可能把一个完整的event分成两半，所以要预先处理
        // 一个问题就是中断的信息交给分开的两段中的哪一段来处理
        // 这里采用的方法是：哪一段时间较长就由它来处理，此时如果中断时间仍大于该段时间，则产生运行错误
        long last;
        if ( index != 0 ) {
            last = ((Entry)entry.elementAt(index-1)).time.getTime();
        } else last = 0;    // 若index==0，last其实就没有什么用了
        long next = currentry.time.getTime();
        long curr = start.getTime();
        // 生成一个新的时间点，用来分割这个event
        Entry startentry = new Entry();
        startentry.time = start;
        // 判断中断的信息应该交给谁来处理，相等则归到后一段
        if ( index==0 || (next-curr)>=(curr-last) ) {
            event = new EventEntry( startentry, currentry );
        } else { // index!=0 && (next-curr)<(curr-last)
            // 这是说，把中断的信息交给上一段处理
            Entry en = (Entry)currentry.clone();
            en.breakNum = 0;
            en.breakTime = 0;
            event = new EventEntry( startentry, en );
        }
        if ( event.breakTime > event.duration )
            // 用UnsupportedOperationException是因为本来可以处理，只是没有再处理而已
            throw new UnsupportedOperationException( "Segment.generate: start BreakTime > Duration" );
        summarize( event );

        // 开始处理正常情况的事件
        index++;
        Entry lastentry = currentry;
        // 当end同currentry相等时，看作currentry after end，取用end的时间。所以下面用.before而不是!.after
        while ( index<entry.size() && (currentry=(Entry)entry.elementAt(index)).time.before(end) ) {
            event = new EventEntry( lastentry, currentry );
            summarize( event );
            lastentry = currentry;
            index++;
        }

        // 收尾工作
        if ( index==entry.size() ) { // 全都读完了
            endDate = currentry.time;   // 设置结束时间为最后一条entry的时间
            return;
        }
        last = lastentry.time.getTime();
        next = currentry.time.getTime();
        curr = end.getTime();
        Entry endentry = (Entry)currentry.clone();
        endentry.time = end;
        // 相等则中断信息归到后一段
        if ( curr-last <= next-curr ) {
            endentry.breakNum = 0;
            endentry.breakTime= 0;
        }
        event = new EventEntry( lastentry, endentry );
        if ( event.breakTime > event.duration )
            // 用UnsupportedOperationException是因为本来可以处理，只是没有再处理而已
            throw new UnsupportedOperationException( "Segment.generate: end BreakTime > Duration" );
        summarize( event );
    }

    /**Pre   :
     * Post  :把一个事件统计在一个Segment中
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