package client;

import java.rmi.*;
import java.math.*;
import compute.*;

public class ComputePi {
    public static void main(String args[]) {
    	if ( args.length != 1 ) args = new String[] { "10" };
    	// ����Ҫ��security manager������rmi���صĴ���İ�ȫ��
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            // ���name������url��ʽ���ڿͻ���Ҫͨ�����url������
            String name = "//neptune/Compute";
            // comp��һ��remote reference����һ��stub����rmi registry���ҵ���
            Compute comp = (Compute) Naming.lookup(name);
            Pi task = new Pi(Integer.parseInt(args[0]));
            // ���õ�ʱ���task���л������͵�remote��
            BigDecimal pi = (BigDecimal) (comp.executeTask(task));
            System.out.println(pi);
        } catch (Exception e) {
            System.err.println("ComputePi exception: " + e.getMessage());
            e.printStackTrace();
        }
    }    
}
