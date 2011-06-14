/***************************************************************
 * ProgramID:   JAPP01-02.
 * Project:     MyTime2.
 * Version:     beta 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-14.
 * Developer:   Cobra.
 * Description: 表示HanDBase中的一条记录，组成EntrySeries
 * Copyright:   GPL.
****************************************************************/

import java.util.*;
import myUtils.*;
import java.text.*;

class Entry
{
    public Date time;       // 包含日期信息，表示事件的结束时刻
    public EventType type;  // 事件的类型
    public int eff;         // 事件的效率
    public int breakNum;    // 中断的次数
    public int breakTime;   // 中断的时间总和
    public String desc;     // 事件的描述

    public Object clone()
    {
        Entry entry = new Entry();
        entry.time = (this.time==null)?null:(Date)this.time.clone();
        entry.type = (this.type==null)?null:(EventType)this.type.clone();
        entry.eff  = this.eff;
        entry.breakNum = this.breakNum;
        entry.breakTime= this.breakTime;
        entry.desc = (this.desc==null)?null:this.desc.toString();
        return entry;
    }

    /**Pre   :
     * Post  :如果无法parse，返回null，否则返回一个新的Entry实例
     * Usage :
    ** Param :en为需要parse的字符串 */
    public static Entry parse( String en )
    {
        Entry entry = new Entry();
        TokenIterator token = new TokenIterator( "\t", en );
        try {

            String timestring = (String)token.next() + " " + (String)token.next(); // 时间加上日期就可以解析了
            SimpleDateFormat df = new SimpleDateFormat( "hh:mm a yyyy-MM-dd", Locale.ENGLISH ); // 必须加上ENGLISH，否则'AM'要用'上午'
            entry.time = df.parse( timestring );

            entry.type = EventType.parse( (String)token.next() );
            entry.eff  = Integer.parseInt( (String)token.next() );
            if ( entry.eff<1 || entry.eff>100 )
                throw new NumberFormatException("Efficiency should be between 1 and 100");

            String breakstring = (String)token.next();  // break信息可以为空，其它的不能为空
            if ( breakstring != null ) {
                StringTokenizer breaks = new StringTokenizer( breakstring );
                entry.breakNum = breaks.countTokens();
                entry.breakTime= 0;
                for ( int i=0; i<entry.breakNum; i++ )
                    entry.breakTime += Integer.parseInt( breaks.nextToken() );
            } else {
                entry.breakNum = 0;
                entry.breakTime= 0;
            }
            entry.desc = (String)token.next();  // description信息也可为空

        } catch ( ParseException e ) {
            Main.printError( "Parse error: "+e.getMessage() );
            return null;
        } catch ( NoSuchElementException e ) {
            Main.printError( "Fields total number is incorrect" );
            return null;
        } catch ( NullPointerException e ) {
            Main.printError( "One of the field should not be null" );
            return null;
        } catch ( NumberFormatException e ) {
            Main.printError( "Number parse error: "+e.getMessage() );
            return null;
        }

        if ( token.hasNext() ) { // 保证只有规定的字段数
            Main.printError( "Fields total number exceed" );
            return null;
        }

        return entry;
    }

    public void print()
    {
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd h:mm a", Locale.ENGLISH );
        System.out.print( df.format( time ) + "  " );
        System.out.print( (String)EventType.typeTable.elementAt(type.getTypeID())+"\t" );
        System.out.print( eff+"  "+breakNum+"  "+breakTime+"  " );
        System.out.println( desc );
    }
}