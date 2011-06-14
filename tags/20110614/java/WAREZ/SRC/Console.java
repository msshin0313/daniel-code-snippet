/***************************************************************
 * ProgramID:	JAPP02-01.
 * Project:		warez: a collection	of useful utilities.
 * Version:		1.0.
 * Created:		2002-10-26.
 * LastUpdated:	2002-11-2.
 * Developer:	cobra.
 * Description:	�����ĿҲ������������ѧ��design pattern��
				�������ڵ���Щ������ҪΪ�˴�һ��framework�����ڽ�������
				�����������������õ�����Ware.
 * Copyright:	GPL.
****************************************************************/
import java.util.*;
import java.io.*;

class Console
{
	public static void main(String[] args) 
	{
		Console	con	= new Console();
		con.showWelcomeMsg();
		con.registerWarez();
		con.listWarez();
		con.executeWarez();
		con.showByeMsg();
	}

////////////// Instance ///////////////

	private List warezList;	// �������п��õ�warez���б�

	public void	showWelcomeMsg()
	{
		System.out.println();
		System.out.println(	"Welcome to use 'warez'	-- A collection	of useful utilities!" );
		System.out.println();
	}

	public void	registerWarez()
	{
		warezList = new LinkedList();
		warezList.add( new SimpleWarez() );
		warezList.add( new FlattenDir() );
		// register new warez!
	}

	public void listWarez()
	{
		for ( int i=0; i < warezList.size(); i++ ) {
			Warez warez = (Warez) warezList.get( i );
			System.out.println( i+1 + ". " + warez.getName() );
		}
		System.out.println( "0. Exit" );
		System.out.println();
	}

	public void executeWarez()
	{
		int choise = -1;
		BufferedReader in = new BufferedReader( new InputStreamReader(System.in) );
		do {
			System.out.print( "Choose: " );
			try {
				choise = Integer.parseInt( in.readLine() );
			} catch ( Exception e ) {
				choise = -1;
			}
		} while ( choise>warezList.size() || choise<0 );
		if ( choise==0 ) return;

		// ��ʱ���û��Ѿ�ѡ����Ҫִ�е�warez�ı��
		Warez warez = (Warez) warezList.get( choise-1 );
		System.out.println( "\nYou choose: " + warez.getName() );
		System.out.println( warez.getDescription() );
		List params = warez.getParams();

		for ( int i=0; i < params.size(); i++ ) {
			Param param = (Param) params.get( i );
			boolean paramOK = false;
			do {
				System.out.println( param.getPrompt() );
				try {
					param.setString( in.readLine() );
					paramOK = true;
				} catch (Exception e) {
					System.out.println( e.getMessage() );
				}
			} while ( paramOK == false );
		}

		// ��ʱ��paramsҲ�Ѿ����ú���
		warez.execute();
	}

	public void showByeMsg()
	{
		System.out.println();
		System.out.println(	"Thanks for using. Good Luck!" );
		System.out.println();
	}
}