/***************************************************************
 * ProgramID:   JAPP01-07.
 * Project:     MyTime2.
 * Version:     beta 1.0.
 * Created:     2002-7-14.
 * LastUpdated: 2002-7-14.
 * Developer:   Cobra.
 * Description: 用来表示某个事件
 * Copyright:   GPL.
****************************************************************/
import java.util.*;

class EventEntry
{
    public Date begin;
    public int duration;
    public EventType type;
    public int efficiency;
    public int breakNum;
    public int breakTime;
    public String description;

    public EventEntry( Entry start, Entry end )
    {
        begin = start.time;
        //System.err.println("event duration long: "+(start.time.getTime()-end.time.getTime())/60000);
        duration = (int)((end.time.getTime()-start.time.getTime())/60000);   // /60000表示以分钟为单位
        //System.err.println( "event duration: "+duration);
        type = end.type;
        efficiency = end.eff;
        breakNum = end.breakNum;
        breakTime = end.breakTime;
        description = end.desc;
    }
}