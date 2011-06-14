/***************************************************************
 * ProgramID:   JAPP01-04.
 * Project:     MyTime2.
 * Version:     beta 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-17. 加入保证输入文件至少有一条记录
 * Developer:   Cobra.
 * Description: Entry的序列，就是HanDBase中的记录的列表
 * Copyright:   GPL.
****************************************************************/

import java.util.*;
import java.io.*;

class EntrySeries extends Vector
{

    /**Pre   :filename不为空
     * Post  :将文件中的数据读入进来，生成一个EntrySeries
     * Usage :
    ** Param :filename存放读入文件 */
    public void readEntry( File filename ) throws IOException
    {
        assert filename != null;
        LineNumberReader file = new LineNumberReader( new FileReader(filename) ); // 可能throw一个IOException
        String en;

        while( (en=file.readLine()) != null ) {
            if ( en.startsWith("#") ) {
                continue;   // 跳过注释行
            } else if ( en.equals("") ) {
                continue;   // 跳过空行
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


    /**Pre   :已经读入的数据都是正确的
     * Post  :如果entry的信息有错，返回false
     * Usage :
    ** Param :file存放读入文件 */
    private boolean validate( Entry entry )
    {
        assert entry != null;
        if ( elementCount != 0 ) {

            Entry lastEntry = (Entry)elementAt(elementCount-1);
            if ( !lastEntry.time.before(entry.time) ) {  // 确保按照时间顺序排序
                Main.printError( "Current Entry should not predate the last entry" );
                return false;
            }

            Date d = new Date(entry.time.getTime()-43200000);   // 43200000是12小时的miliseconds表示
            if ( d.after(lastEntry.time) ) {  // 确保事件时间不超过12小时
                Main.printError( "Current event should not exeed 12 hours" );
                return false;
            }

            long breaktime = entry.breakTime * 60000;   // 把分钟转换成微秒
            long eventtime = entry.time.getTime()-lastEntry.time.getTime();
            if ( eventtime <= breaktime ) {  // 确保中断的时间小于事件的时间
                Main.printError( "Break time should less than event time" );
                return false;
            }
        }
        // 不妨考虑在这里加上当elementCount==0时，添加一条entry，便于计算
        return true;
    }
}