package client;

import java.rmi.*;
import java.math.*;
import compute.*;

public class ComputePi {
    public static void main(String args[]) {
    	if ( args.length != 1 ) args = new String[] { "10" };
    	// 必须要有security manager来控制rmi下载的代码的安全性
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            // 这个name必须是url格式，在客户端要通过这个url来连接
            String name = "//neptune/Compute";
            // comp是一个remote reference，是一个stub，在rmi registry中找到的
            Compute comp = (Compute) Naming.lookup(name);
            Pi task = new Pi(Integer.parseInt(args[0]));
            // 调用的时候把task串行化，发送到remote端
            BigDecimal pi = (BigDecimal) (comp.executeTask(task));
            System.out.println(pi);
        } catch (Exception e) {
            System.err.println("ComputePi exception: " + e.getMessage());
            e.printStackTrace();
        }
    }    
}
