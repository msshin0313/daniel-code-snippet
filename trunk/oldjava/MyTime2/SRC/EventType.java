/***************************************************************
 * ProgramID:   JAPP01-03.
 * Project:     MyTime2.
 * Version:     beta 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-14.
 * Developer:   Cobra.
 * Description: 记录所有可能的时间类型.
 * Copyright:   GPL.
****************************************************************/

import java.util.*;
import java.text.ParseException;
import java.io.*;

class EventType
{
    public static Vector typeTable; // 记录事件类型的名称顺序
    public static Vector categoryTable; // 记录类目类型的名称顺序
    public static Vector belongTable;   // type同category的对应表
    protected int typeid = -1;   // 记录当前对象是那个type

    public Object clone()
    {
        EventType et = new EventType();
        et.typeid = this.typeid;
        return et;
    }

    /**Pre   :Types.cfg已经正确设置
     * Post  :填充typeTable等等，生成正确的类型信息
     * Usage :
    ** Param :*/
    static  // load static data
    {
        typeTable     = new Vector();
        categoryTable = new Vector();
        belongTable   = new Vector();
        Properties prop = new Properties();
        try {
            prop.load( new FileInputStream("Types.cfg") );
        } catch ( IOException e ) {
            Main.printError( "Types.cfg file read error" );
            System.exit(2);
        }
        Enumeration categoryNames = prop.propertyNames();
        while ( categoryNames.hasMoreElements() ) {

            String cName = (String)categoryNames.nextElement();
            categoryTable.addElement( cName );  // 注册category
            StringTokenizer types = new StringTokenizer( prop.getProperty(cName) );

            while ( types.hasMoreTokens() ) {
                String tName = types.nextToken();
                typeTable.addElement( tName );  // 注册type
                belongTable.addElement( new Integer(categoryTable.size()-1) );  // 把当前type注册为当前category的一类
            }
        }
        //for ( int i=0; i<categoryTable.size(); i++ ) System.out.println( categoryTable.elementAt(i) );
        //for ( int i=0; i<typeTable.size(); i++ ) System.out.println( typeTable.elementAt(i) );
        //for ( int i=0; i<belongTable.size(); i++ ) System.out.println( belongTable.elementAt(i) );
    }


    /**Pre   :
     * Post  :如果类型名正确，生成一个类型实例，否则返回null
     * Usage :
    ** Param : */
    public static EventType parse( String typename ) throws ParseException
    {
        if ( typename == null )
            throw new NullPointerException();
        EventType type = new EventType();
        type.typeid = typeTable.indexOf( typename );
        if ( type.typeid == -1 )
            throw new ParseException("Can not parse Type", 0);
        return type;
    }

    public final int getTypeID()
    {
        return typeid;
    }

    public final int getCategoryID()
    {
        Integer catid = (Integer)belongTable.elementAt(typeid);
        return catid.intValue();
    }

}