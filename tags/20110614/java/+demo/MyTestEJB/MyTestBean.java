// bean implementation file

import javax.ejb.*;
import javax.naming.*;

public class MyTestBean implements SessionBean{
	
	String name;
	
	// such ejbCreate() should 'throws CreateException'
	// the argument must match those defined in the Home interface
	public void ejbCreate(String name) throws CreateException {
		this.name = name;
	}
	
	public void ejbCreate() throws CreateException {
		// using environment variables
		String str = null;
		try {
			Context ic = new InitialContext();
			Context env = (Context)ic.lookup("java:comp/env");
			str = (String)env.lookup("Default Name");
		} catch ( NamingException e ) {
			e.printStackTrace();
			str = "DefaultInCode";
		}
		ejbCreate( str );
	}
	
	// must be public
	// throw a EJBException to be wrapped by container as a RemoteException, 
	// which could be caught by the client.
	public String getName() {
		return name;
	}
	
	public int add( int a, int b ) {
		return a+b;
	}
	
	public MyTestBean() {}
	public void ejbRemove() {}
	public void ejbActivate() {}
	public void ejbPassivate() {}
	public void setSessionContext(SessionContext sc) {}
	
}