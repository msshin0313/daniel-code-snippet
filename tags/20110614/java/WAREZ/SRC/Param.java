/***************************************************************
 * ProgramID:	JAPP02-04.
 * Project:		warez: a collection	of useful utilities.
 * Version:		1.0.
 * Created:		2002-11-2.
 * LastUpdated:	2002-11-2.
 * Developer:	cobra.
 * Description:	����������������Ľӿ�
 * Copyright:	GPL.
****************************************************************/

public interface Param
{
	String getPrompt();		// ��������ṩparam����ʾ��Ϣ
	void setString( String input ) throws Exception;	// ����param�����봮�����д���throw������������ʾ
	Object getParam();		// ������߷��ظ�param�����ͣ�ͨ����String, File��
}