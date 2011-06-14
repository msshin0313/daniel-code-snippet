// remote interface

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface MyTest extends EJBObject {
   // each method must throws RemoteException
   
   // get the name defined in 'create()' 
   public String getName() throws RemoteException;
   
   // add two integers
   public int add(int a, int b) throws RemoteException;
   
}
