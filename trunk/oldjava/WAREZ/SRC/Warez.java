/***************************************************************
 * ProgramID:	JAPP02-02.
 * Project:		warez: a collection	of useful utilities.
 * Version:		1.0.
 * Created:		2002-10-26.
 * LastUpdated:	2002-11-2.
 * Developer:	cobra.
 * Description:	Warez的基类，实现基本功能，所有的warez都要从这里派生
 * Copyright:	GPL.
****************************************************************/
import java.util.*;

public abstract class Warez
{
	// methods must be overriden.
	public abstract String getName();			// 用于返回一个简短的名称，供Console来List
	public abstract String getDescription();	// 返回较长的描述信息，包括参数用法等等
	public abstract void   execute();
	protected abstract void registerParams();		// register params here. using 'params.add( Param )'

	// you'll never override methods below
	protected List params;
	public Warez()
	{
		params = new LinkedList();
		registerParams();
	}
	public List getParams()
	{
		return params;
	}
}