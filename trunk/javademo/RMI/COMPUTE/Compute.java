package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

// remote object�Ľӿڣ���client��˵��ֻ�ܷ������ﶨ���method
// ��������Remote����һ��tag interface��
public interface Compute extends Remote {
    // ����throws RemoteException
    Object executeTask(Task t) throws RemoteException;
}
