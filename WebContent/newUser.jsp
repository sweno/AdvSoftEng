<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<%@ include file="headcontent.jspf" %>
<body>
<%@ include file="header.jspf" %>
<div class="container-fluid">
    <div class="row row-content">
        <%@ include file="sidebar.jspf" %>
        <div class="content col-md-5 col-md-offset-2">
            <c:if test="${problems != null}"><c:forEach items="${problems}" var="problem"><p>${problem}.</p></c:forEach></c:if>

	<p><h1>Please fill in the remaining data to complete user registration.</h1></p>
	
	<form action="${pageContext.request.contextPath}/newUser" method="post">
        <label for="name">Company Name : </label> <input type="text" name="name" id="name" value="<%= request.getParameter("name") %>"><br>
        <label for="email">Email: </label> <input type="text" name="email" id="email" value="<%= request.getParameter("email") %>"><br>
        <label for="password">Password:</label> <input type="password" name="password" id="password" value="<%= request.getParameter("pass") %>"><br>
        <label for="Buyer">I will be buying: </label> <input type="checkbox" name="Buyer" id="Buyer">
        <label for="Seller">I will be selling: </label> <input type="checkbox" name="Seller" id="Seller"><br>
        Billing Address:<br>
        <label for="Bill_Street1">Street: </label> <input type="text" name="Bill_Street1" id="Bill_Street1" value=""><br>
        <label for="Bill_Street2">Street: </label> <input type="text" name="Bill_Street2" id="Bill_Street2" value=""><br>
        <label for="Bill_City">City: </label> <input type="text" name="Bill_City" id="Bill_City" value=""><br>
        <label for="Bill_State">State: </label> <input type="text" name="Bill_State" id="Bill_State" value=""><br>
        <label for="Bill_Zip">Postal Code: </label> <input type="text" name="Bill_Zip" id="Bill_Zip" value=""><br>
        <label for="Bill_Country">Country: </label> <input type="text" name="Bill_Country" id="Bill_Country" value=""><br>
        Shipping Address: <br>
        <label for="SameShip">Shipping address is the same as billing: </label> <input type="checkbox" name="SameShip" id="SameShip"><br>
        <label for="Ship_Street1">Street: </label> <input type="text" name="Ship_Street1" id="Ship_Street1" value=""><br>
        <label for="Ship_Street2">Street: </label> <input type="text" name="Ship_Street2" id="Ship_Street2" value=""><br>
        <label for="Ship_City">City: </label> <input type="text" name="Ship_City" id="Ship_City" value=""><br>
        <label for="Ship_State">State: </label> <input type="text" name="Ship_State" id="Ship_State" value=""><br>
        <label for="Ship_Zip">Postal Code: </label> <input type="text" name="Ship_Zip" id="Ship_Zip" value=""><br>
        <label for="Ship_Country">Country: </label> <input type="text" name="Ship_Country" id="Ship_Country" value=""><br>
        <input type="submit" name="signup" value="Sign Up">
    </form>
        </div>
    </div>
</div>
<%@ include file="footer.jspf" %>
</body>
</html>
