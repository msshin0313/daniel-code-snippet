/***************************************************************
 * ProgramID:   JAPP01-03.
 * Project:     MyTime2.
 * Version:     beta 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-14.
 * Developer:   Cobra.
 * Description: ��¼���п��ܵ�ʱ������.
 * Copyright:   GPL.
****************************************************************/

import java.util.*;
import java.text.ParseException;
import java.io.*;

class EventType
{
    public static Vector typeTable; // ��¼�¼����͵�����˳��
    public static Vector categoryTable; // ��¼��Ŀ���͵�����˳��
    public static Vector belongTable;   // typeͬcategory�Ķ�Ӧ��
    protected int typeid = -1;   // ��¼��ǰ�������Ǹ�type

    public Object clone()
    {
        EventType et = new EventType();
        et.typeid = this.typeid;
        return et;
    }

    /**Pre   :Types.cfg�Ѿ���ȷ����
     * Post  :���typeTable�ȵȣ�������ȷ��������Ϣ
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
            categoryTable.addElement( cName );  // ע��category
            StringTokenizer types = new StringTokenizer( prop.getProperty(cName) );

            while ( types.hasMoreTokens() ) {
                String tName = types.nextToken();
                typeTable.addElement( tName );  // ע��type
                belongTable.addElement( new Integer(categoryTable.size()-1) );  // �ѵ�ǰtypeע��Ϊ��ǰcategory��һ��
            }
        }
        //for ( int i=0; i<categoryTable.size(); i++ ) System.out.println( categoryTable.elementAt(i) );
        //for ( int i=0; i<typeTable.size(); i++ ) System.out.println( typeTable.elementAt(i) );
        //for ( int i=0; i<belongTable.size(); i++ ) System.out.println( belongTable.elementAt(i) );
    }


    /**Pre   :
     * Post  :�����������ȷ������һ������ʵ�������򷵻�null
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