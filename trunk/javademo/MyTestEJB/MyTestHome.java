// home interface

import java.io.Serializable;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface MyTestHome extends EJBHome {
 
    MyTest create(String name) throws RemoteException, CreateException;
    MyTest create() throws RemoteException, CreateException; 
    
}
