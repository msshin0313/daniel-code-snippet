/***************************************************************
 * ProgramID:   JAPP01-02.
 * Project:     MyTime2.
 * Version:     beta 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-14.
 * Developer:   Cobra.
 * Description: ��ʾHanDBase�е�һ����¼�����EntrySeries
 * Copyright:   GPL.
****************************************************************/

import java.util.*;
import myUtils.*;
import java.text.*;

class Entry
{
    public Date time;       // ����������Ϣ����ʾ�¼��Ľ���ʱ��
    public EventType type;  // �¼�������
    public int eff;         // �¼���Ч��
    public int breakNum;    // �жϵĴ���
    public int breakTime;   // �жϵ�ʱ���ܺ�
    public String desc;     // �¼�������

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
     * Post  :����޷�parse������null�����򷵻�һ���µ�Entryʵ��
     * Usage :
    ** Param :enΪ��Ҫparse���ַ��� */
    public static Entry parse( String en )
    {
        Entry entry = new Entry();
        TokenIterator token = new TokenIterator( "\t", en );
        try {

            String timestring = (String)token.next() + " " + (String)token.next(); // ʱ��������ھͿ��Խ�����
            SimpleDateFormat df = new SimpleDateFormat( "hh:mm a yyyy-MM-dd", Locale.ENGLISH ); // �������ENGLISH������'AM'Ҫ��'����'
            entry.time = df.parse( timestring );

            entry.type = EventType.parse( (String)token.next() );
            entry.eff  = Integer.parseInt( (String)token.next() );
            if ( entry.eff<1 || entry.eff>100 )
                throw new NumberFormatException("Efficiency should be between 1 and 100");

            String breakstring = (String)token.next();  // break��Ϣ����Ϊ�գ������Ĳ���Ϊ��
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
            entry.desc = (String)token.next();  // description��ϢҲ��Ϊ��

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

        if ( token.hasNext() ) { // ��ֻ֤�й涨���ֶ���
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