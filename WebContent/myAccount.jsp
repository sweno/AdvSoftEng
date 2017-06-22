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
            <center><h1> My Account </h1></center>
				<form action="${pageContext.request.contextPath}/updateUser" method="post">
        <label for="name">Company Name : </label> <input type="text" name="name" id="name" value="<c:out value="${user.name}"/>"><br>
        Email: <c:out value="${user.email}"/><br>
        Password: <a href="${pageContext.request.contextPath}/changePassword.jsp">Change Password</a><br>
        Business Type: <label for="Buyer">Buyer: </label> <input type="checkbox" name="Buyer" id="Buyer" <c:if test="${user.getBuyerFlag()}">checked</c:if>> 
        &nbsp;<label for="Seller">Seller: </label> <input type="checkbox" name="Seller" id="Seller" <c:if test="${user.getSellerFlag()}">checked</c:if>><br>
        Billing Address:<br>
        <label for="Bill_Street1">Street: </label> <input type="text" name="Bill_Street1" id="Bill_Street1" value="<c:out value="${user.getBillingAddress().getStreet1()}"/>"><br>
        <label for="Bill_Street2">Street: </label> <input type="text" name="Bill_Street2" id="Bill_Street2" value="<c:out value="${user.getBillingAddress().getStreet2()}"/>"><br>
        <label for="Bill_City">City: </label> <input type="text" name="Bill_City" id="Bill_City" value="<c:out value="${user.getBillingAddress().getCity()}"/>"><br>
        <label for="Bill_State">State: </label> <input type="text" name="Bill_State" id="Bill_State" value="<c:out value="${user.getBillingAddress().getState()}"/>"><br>
        <label for="Bill_Zip">Postal Code: </label> <input type="text" name="Bill_Zip" id="Bill_Zip" value="<c:out value="${user.getBillingAddress().getZip()}"/>"><br>
        <label for="Bill_Country">Country: </label> <input type="text" name="Bill_Country" id="Bill_Country" value="<c:out value="${user.getBillingAddress().getCountry()}"/>"><br>
        
        Shipping Address: <br>
        <label for="SameShip">Shipping address is the same as billing: </label> <input type="checkbox" name="SameShip" id="SameShip" <c:if test="${user.getShippingIsBilling()}">checked</c:if>><br>
        <c:choose><%-- if the shipping address is null, then referencing parts of it will cause a null pointer exception --%>
        	<c:when test="${not empty user.getShippingAddress()}">
        <label for="Ship_Street1">Street: </label> <input type="text" name="Ship_Street1" id="Ship_Street1" value="<c:out value="${user.getShippingAddress().getStreet1()}"/>"><br>
        <label for="Ship_Street2">Street: </label> <input type="text" name="Ship_Street2" id="Ship_Street2" value="<c:out value="${user.getShippingAddress().getStreet2()}"/>"><br>
        <label for="Ship_City">City: </label> <input type="text" name="Ship_City" id="Ship_City" value="<c:out value="${user.getShippingAddress().getCity()}"/>"><br>
        <label for="Ship_State">State: </label> <input type="text" name="Ship_State" id="Ship_State" value="<c:out value="${user.getShippingAddress().getState()}"/>"><br>
        <label for="Ship_Zip">Postal Code: </label> <input type="text" name="Ship_Zip" id="Ship_Zip" value="<c:out value="${user.getShippingAddress().getZip()}"/>"><br>
        <label for="Ship_Country">Country: </label> <input type="text" name="Ship_Country" id="Ship_Country" value="<c:out value="${user.getShippingAddress().getCountry()}"/>"><br>
        	</c:when>
        	<c:otherwise><%-- just put in the base HTML --%>
        <label for="Ship_Street1">Street: </label> <input type="text" name="Ship_Street1" id="Ship_Street1" value=""><br>
        <label for="Ship_Street2">Street: </label> <input type="text" name="Ship_Street2" id="Ship_Street2" value=""><br>
        <label for="Ship_City">City: </label> <input type="text" name="Ship_City" id="Ship_City" value=""><br>
        <label for="Ship_State">State: </label> <input type="text" name="Ship_State" id="Ship_State" value=""><br>
        <label for="Ship_Zip">Postal Code: </label> <input type="text" name="Ship_Zip" id="Ship_Zip" value=""><br>
        <label for="Ship_Country">Country: </label> <input type="text" name="Ship_Country" id="Ship_Country" value=""><br>
        	</c:otherwise>
        </c:choose>
        <input type="submit" name="update" value="Update">
    </form>
        </div>
    </div>
</div>
<%@ include file="footer.jspf" %>
</body>
</html>
