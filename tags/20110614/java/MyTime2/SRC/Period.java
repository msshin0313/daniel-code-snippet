/***************************************************************
 * ProgramID:   JAPP01-06.
 * Project:     MyTime2.
 * Version:     alpha 1.0.
 * Created:     2002-7-11.
 * LastUpdated: 2002-7-15
 * Developer:   Cobra.
 * Description: 包含了若干互不重叠的Segment，用来比较这些Segment的最大、最小值等等。通常只有一个实例
 * Copyright:   GPL.
****************************************************************/

class Period
{
    protected Segment[] segments;
    public int typeAvg[];
    public int categoryAvg[];

    public Period( Segment[] seg )
    {
        assert seg != null;
        assert seg.length != 0;
        segments = seg;
        typeAvg     = new int[ EventType.typeTable.size() ];
        categoryAvg = new int[ EventType.categoryTable.size() ];
    }

    public void average()
    {
        int sum = 0;
        for ( int typeIndex=0; typeIndex<typeAvg.length; typeIndex++ ) {
            for ( int segIndex=0; segIndex<segments.length; segIndex++ ) {
                sum += segments[segIndex].typeSum[typeIndex];
            }
            typeAvg[typeIndex] = sum / segments.length;
            sum = 0;
        }
        sum = 0;
        for ( int catIndex=0; catIndex<categoryAvg.length; catIndex++ ) {
            for ( int segIndex=0; segIndex<segments.length; segIndex++ ) {
                sum += segments[segIndex].catSum[catIndex];
            }
            categoryAvg[catIndex] = sum / segments.length;
            sum = 0;
        }
    }

    public Segment[] sort( int field )
    {
        assert ( field>=0 && field<=EventType.typeTable.size() )
            || ( field>=100 && field<=EventType.categoryTable.size() );
        Segment[] sorted = new Segment[ segments.length ];

        return sorted;
    }
}