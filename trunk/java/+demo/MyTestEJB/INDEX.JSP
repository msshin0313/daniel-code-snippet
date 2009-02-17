<%-- Web Client to acess MyTestEJB --%>

<%@ page import="MyTest, MyTestHome, javax.ejb.*, javax.naming.*, javax.rmi.PortableRemoteObject, java.rmi.RemoteException" %>
<%!
   private MyTest t1 = null;

   public void jspInit() { 
      try {
         InitialContext ic = new InitialContext();
         Object objRef = ic.lookup("java:comp/env/ejb/TheMyTest_jsp");
         MyTestHome home = (MyTestHome)PortableRemoteObject.narrow(objRef, MyTestHome.class);
         t1 = home.create();
      } catch (RemoteException ex) {
            System.out.println("Couldn't create bean."+ ex.getMessage());
      } catch (CreateException ex) {
            System.out.println("Couldn't create bean."+ ex.getMessage());
      } catch (NamingException ex) {
            System.out.println("Unable to lookup home" + ex.getMessage());
      } 
   }

   public void jspDestroy() {    
         t1 = null;
   }
%>
<html>
<head>
    <title>MyTest</title>
</head>

<body bgcolor="white">
<h1><b><center>MyTest</center></b></h1>
<hr>
<p>Enter an amount to add:</p>
<form method="get">
<input type="text" name="a" size="25">
<input type="text" name="b" size="25">
<br>
<p>
<input type="submit" value="Submit">
<input type="reset" value="Reset">
</form>

<%
try {
    String sa = request.getParameter("a");
    String sb = request.getParameter("b");
    if ( sa != null && sa.length() > 0 && sb != null && sb.length() > 0) {
       int a = Integer.parseInt(sa);
       int b = Integer.parseInt(sb);
%>
   <p>Result is: <%= t1.add( a, b ) %> </p>
<%
    }
} catch( Exception ex ) {
%>
	<p> Cannot parse number </p>
<%
}  
%>

</body>
</html>
