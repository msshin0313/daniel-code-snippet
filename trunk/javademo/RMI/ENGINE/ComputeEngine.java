package engine;

import java.rmi.*;
import java.rmi.server.*;
import compute.*;

// UnicastRemoteObject��Ȼ���Ǳ���ģ���������ϣ��ȽϷ���
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
    	// ���붨��security manager������client���͹�����object��Ȩ��
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        String name = "//neptune/Compute";
        try {
            Compute engine = new ComputeEngine();
            // ��stringͬ�����reference����һ��ֻҪregistry��������engine�Ͳ��ᱻgarbage collected
            Naming.rebind(name, engine);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
