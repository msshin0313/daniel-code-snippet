package engine;

import java.rmi.*;
import java.rmi.server.*;
import compute.*;

// UnicastRemoteObject虽然不是必须的，但最好用上，比较方便
public class ComputeEngine extends UnicastRemoteObject
                           implements Compute
{
    public ComputeEngine() throws RemoteException {
        super();
    }

    public Object executeTask(Task t) {
        return t.execute();
    }

    public static void main(String[] args) {
    	// 必须定义security manager来管理client发送过来的object的权限
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        String name = "//neptune/Compute";
        try {
            Compute engine = new ComputeEngine();
            // 将string同对象的reference绑定在一起，只要registry不撤销，engine就不会被garbage collected
            Naming.rebind(name, engine);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
