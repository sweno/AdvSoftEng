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

<form action="${pageContext.request.contextPath}/newUser" method="post">
        <label for="name">Company Name : </label> <input type="text" name="name" id="name" value=""><br>
        <label for="email">Email: </label> <input type="text" name="email" id="email" value=""><br>
        <label for="password">Password:</label> <input type="password" name="password" id="password" value=""><br>
        <label for="Buyer">I will be buying: </label> <input type="checkbox" name="Buyer" id="Buyer" value="">
        <label for="Seller">I will be selling: </label> <input type="checkbox" name="Seller" id="Seller" value=""><br>
        Billing Address:<br>
        <label for="Bill_Street1">Street: </label> <input type="text" name="Bill_Street1" id="Bill_Street1" value=""><br>
        <label for="Bill_Street2">Street: </label> <input type="text" name="Bill_Street2" id="Bill_Street2" value=""><br>
        <label for="Bill_City">City: </label> <input type="text" name="Bill_City" id="Bill_City" value=""><br>
        <label for="Bill_State">State: </label> <input type="text" name="Bill_State" id="Bill_State" value=""><br>
        <label for="Bill_Zip">Postal Code: </label> <input type="text" name="Bill_Zip" id="Bill_Zip" value=""><br>
        <label for="Bill_Country">Country: </label> <input type="text" name="Bill_Country" id="Bill_Country" value=""><br>
        Shipping Address: <br>
        <label for="SameShip">Shipping address is the same as billing: </label> <input type="checkbox" name="SameShip" id="SameShip" value=""><br>
        <label for="Ship_Street1">Street: </label> <input type="text" name="Ship_Street1" id="Ship_Street1" value=""><br>
        <label for="Ship_Street2">Street: </label> <input type="text" name="Ship_Street2" id="Ship_Street2" value=""><br>
        <label for="Ship_City">City: </label> <input type="text" name="Ship_City" id="Ship_City" value=""><br>
        <label for="Ship_State">State: </label> <input type="text" name="Ship_State" id="Ship_State" value=""><br>
        <label for="Ship_Zip">Postal Code: </label> <input type="text" name="Ship_Zip" id="Ship_Zip" value=""><br>
        <label for="Ship_Country">Country: </label> <input type="text" name="Ship_Country" id="Ship_Country" value=""><br>
        <input type="submit" name="signup" value="Sign Up">
    </form>
</body>
</html>