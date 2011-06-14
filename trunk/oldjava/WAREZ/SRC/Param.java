/***************************************************************
 * ProgramID:	JAPP02-04.
 * Project:		warez: a collection	of useful utilities.
 * Version:		1.0.
 * Created:		2002-11-2.
 * LastUpdated:	2002-11-2.
 * Developer:	cobra.
 * Description:	用来定义输入参数的接口
 * Copyright:	GPL.
****************************************************************/

public interface Param
{
	String getPrompt();		// 向调用者提供param的提示信息
	void setString( String input ) throws Exception;	// 设置param的输入串，若有错，则throw，包括错误提示
	Object getParam();		// 向调用者返回该param的类型，通常是String, File等
}