/***************************************************************
 * ProgramID:	JAPP02-02.
 * Project:		warez: a collection	of useful utilities.
 * Version:		1.0.
 * Created:		2002-10-26.
 * LastUpdated:	2002-11-2.
 * Developer:	cobra.
 * Description:	Warez�Ļ��࣬ʵ�ֻ������ܣ����е�warez��Ҫ����������
 * Copyright:	GPL.
****************************************************************/
import java.util.*;

public abstract class Warez
{
	// methods must be overriden.
	public abstract String getName();			// ���ڷ���һ����̵����ƣ���Console��List
	public abstract String getDescription();	// ���ؽϳ���������Ϣ�����������÷��ȵ�
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