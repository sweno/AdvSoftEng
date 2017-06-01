<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User Sign Up</title>
</head>
<body>
    <h1>User Sign Up</h1>

    <c:if test="${problems != null}">
        <c:forEach items="${problems}" var="problem">
            <p>${problem}.</p>
        </c:forEach>
    </c:if>

<!-- action="${pageContext.request.contextPath}/newUser" -->    
<form action="${pageContext.request.contextPath}/newUser" method="post">
        <label for="name">Name : </label>
        <input type="text" name="name" id="name" value="">
        <label for="email">Email: </label>
        <input type="text" name="email" id="email" value="">
        <label for="password">Password:</label>
        <input type="text" name="password" id="password" value="">
        <input type="submit" name="signup" value="Sign Up">
    </form>
</body>
</html>