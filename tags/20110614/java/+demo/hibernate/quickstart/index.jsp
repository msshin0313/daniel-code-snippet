<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*, java.util.Date,
                                                                         net.sf.hibernate.HibernateException,
                                                                         net.sf.hibernate.Session,
                                                                         net.sf.hibernate.examples.quickstart.HibernateUtil,
                                                                         net.sf.hibernate.Transaction,
                                                                         net.sf.hibernate.examples.quickstart.Cat" errorPage="" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>My Test Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>

<body>

<h1>Welcome to my test page </h1>
<hr>
<p>Current time: <%= new Date() %></p>
<hr>
<p>
<%
    try {
        Class.forName("net.sf.hibernate.Session");
        out.println("Class Found! Congratulations!");
    } catch (ClassNotFoundException e) {
        throw new RuntimeException();
        //out.println("Class Not Found.");
    }
    String name = request.getParameter("name");
    String ssex = request.getParameter("sex");
    String sweight = request.getParameter("weight");
    if (name==null || ssex==null || sweight==null) throw new IllegalArgumentException();

    char sex = ssex.charAt(0);
    float weight = Float.parseFloat(sweight);
    try {
        Session hsession = null;
        hsession = HibernateUtil.currentSession();
        Transaction tx = hsession.beginTransaction();
        Cat newborn = new Cat();
        newborn.setName(name);
        newborn.setSex(sex);
        newborn.setWeight(weight);
        hsession.save(newborn);
        tx.commit();
        HibernateUtil.closeSession();
        out.println("Insert successfully.");
    } catch (HibernateException e) {
        throw new RuntimeException(e);
    }
    
%>
</p>
</body>
</html>
