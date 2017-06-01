<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome page</title>
</head>
<body>
<p>this is the basic welcome/login page</p>
<p><a href="${pageContext.request.contextPath}/newUser.jsp">click here</a> to create an account</p>
<form action="${pageContext.request.contextPath}/loginSV" method="post">
        <label for="email">Email: </label>
        <input type="text" name="email" id="email" value=""><br>
        <label for="password">Password:</label>
        <input type="text" name="password" id="password" value=""><br>
        <input type="submit" name="login" value="log in">
    </form></body>
</html>