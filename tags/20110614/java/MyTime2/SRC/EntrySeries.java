/***************************************************************
 * ProgramID:   JAPP01-04.
 * Project:     MyTime2.
 * Version:     beta 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-17. ���뱣֤�����ļ�������һ����¼
 * Developer:   Cobra.
 * Description: Entry�����У�����HanDBase�еļ�¼���б�
 * Copyright:   GPL.
****************************************************************/

import java.util.*;
import java.io.*;

class EntrySeries extends Vector
{

    /**Pre   :filename��Ϊ��
     * Post  :���ļ��е����ݶ������������һ��EntrySeries
     * Usage :
    ** Param :filename��Ŷ����ļ� */
    public void readEntry( File filename ) throws IOException
    {
        assert filename != null;
        LineNumberReader file = new LineNumberReader( new FileReader(filename) ); // ����throwһ��IOException
        String en;

        while( (en=file.readLine()) != null ) {
            if ( en.startsWith("#") ) {
                continue;   // ����ע����
            } else if ( en.equals("") ) {
                continue;   // ��������
            }
            Entry entry = Entry.parse( en );
            if ( entry == null ) {
                throw new IOException( "Parse entry error at Line: " + file.getLineNumber() );
            } else if ( !validate(entry) ) {
                throw new IOException( "Entry series error at Line: " + file.getLineNumber() );
            } else {
                addElement( entry );
            }
        }
        file.close();
        if ( size() == 0 ) throw new IOException( "EntrySeries.readEntry: no entry at all" );
    }


    /**Pre   :�Ѿ���������ݶ�����ȷ��
     * Post  :���entry����Ϣ�д�����false
     * Usage :
    ** Param :file��Ŷ����ļ� */
    private boolean validate( Entry entry )
    {
        assert entry != null;
        if ( elementCount != 0 ) {

            Entry lastEntry = (Entry)elementAt(elementCount-1);
            if ( !lastEntry.time.before(entry.time) ) {  // ȷ������ʱ��˳������
                Main.printError( "Current Entry should not predate the last entry" );
                return false;
            }

            Date d = new Date(entry.time.getTime()-43200000);   // 43200000��12Сʱ��miliseconds��ʾ
            if ( d.after(lastEntry.time) ) {  // ȷ���¼�ʱ�䲻����12Сʱ
                Main.printError( "Current event should not exeed 12 hours" );
                return false;
            }

            long breaktime = entry.breakTime * 60000;   // �ѷ���ת����΢��
            long eventtime = entry.time.getTime()-lastEntry.time.getTime();
            if ( eventtime <= breaktime ) {  // ȷ���жϵ�ʱ��С���¼���ʱ��
                Main.printError( "Break time should less than event time" );
                return false;
            }
        }
        // ����������������ϵ�elementCount==0ʱ�����һ��entry�����ڼ���
        return true;
    }
}