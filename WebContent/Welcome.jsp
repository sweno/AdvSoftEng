<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <title>New User Welcome</title>
</head>
<body>
    <h1>Thanks for signing up!</h1>
    <p><strong>Name:</strong> ${name}</p>
    <p><strong>Email: </strong>${email}</p>
    <p>please <a href="${pageContext.request.contextPath}/Index.jsp">login</a></p>
</body>
</html>