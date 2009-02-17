package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

// remote object的接口，对client来说，只能访问这里定义的method
// 必须来自Remote（是一个tag interface）
public interface Compute extends Remote {
    // 必须throws RemoteException
    Object executeTask(Task t) throws RemoteException;
}
