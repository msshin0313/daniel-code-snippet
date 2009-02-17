// client that calls the ejb

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

public class MyTestClient {
	
	public static void main( String[] args ) {
    	try {
    		Context ic = new InitialContext();
    		Context env = (Context)ic.lookup("java:comp/env");
    		Object objref = env.lookup("ejb/TheMyTest");
    		// s: MyTestHome can be located by classloader in the stub class 
    		MyTestHome home = 
    			(MyTestHome)PortableRemoteObject.narrow( objref, MyTestHome.class );
    		
    		MyTest t1 = home.create();
    		System.out.println( t1.getName() );
    		System.out.println( t1.add(3,4) );
    		
    		MyTest t2 = home.create("UserDefinedName");
    		// here still print "DefaultInEnviroment"
    		System.out.println( t2.getName() );
    		System.out.println( t2.add(300,-200) );
	    }
	    catch (Exception ex) {
	    	ex.printStackTrace();
	    }
    }
    
}