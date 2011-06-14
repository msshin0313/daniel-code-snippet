<%@ page import="javax.naming.Context,
                 javax.naming.InitialContext"%>
<%@ page contentType="text/html; charset=gb2312" language="java" errorPage="" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>My Test Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>

<body>
<c:set var="greeting" scope="session" value="Welcome to my test page"/>
<h1>${greeting}</h1>
<hr>
<p><jsp:expression>new java.util.Date()</jsp:expression></p>
<hr>

<p><a href="cat">cat</a></p>
<p><a href="invoker">invoker</a></p>
</body>
</html>
