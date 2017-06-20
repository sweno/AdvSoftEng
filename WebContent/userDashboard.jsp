<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>overview of user account</title>
</head>
<body>
    <c:if test="${problems != null}">
        <c:forEach items="${problems}" var="problem">
            <p>${problem}.</p>
        </c:forEach>
    </c:if>

<p>Overview of user account for <c:out value="${user.name}"/></p>
<form action="${pageContext.request.contextPath}/logout" method="post">
	<input type="submit" name="Logout" value="Logout">
</form>


<span>account information:</span>
	<form action="${pageContext.request.contextPath}/updateUser" method="post">
        <label for="name">Company Name : </label> <input type="text" name="name" id="name" value="<c:out value="${user.name}"/>"><br>
        Email: <c:out value="${user.email}"/><br>
        Password: <a href="${pageContext.request.contextPath}/changepassword.jsp">Change Password</a><br>
        <label for="Buyer">I will be buying: </label> <input type="checkbox" name="Buyer" id="Buyer" <c:if test="${user.getBuyerFlag()}">checked</c:if>>
        <label for="Seller">I will be selling: </label> <input type="checkbox" name="Seller" id="Seller" <c:if test="${user.getSellerFlag()}">checked</c:if>><br>
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
<c:if test="${user.getSellerFlag()}"><br><%-- only try and reference seller information if the user is a seller --%>
<div>Seller information:<br/>
<div>Products on File:<br/>
	<table border="0" cellpadding="5">
		<tr>
			<th>Name</th>
			<th>Description</th>
			<th>Quantity in Stock</th>
			<th>Keywords</th>
			<th>Actions</th>
		</tr>
		<c:forEach var="item" items="${sellerItemList}">
			<tr>
				<td><c:out value="${item.getName()}" /></td>
				<td><c:out value="${item.getDescription()}" /></td>
				<td><c:out value="${item.getQuantityInStock()}" /></td>
				<td><c:out value="${item.getKeywords()}" /></td>
				<td>
				<form action="${pageContext.request.contextPath}/newListing" method="post">
					<input type="hidden" name="id" value="<c:out value='${item.getItemID()}'/>"/>
					<input type="submit" name="CreateListing" value="Create Listing">
				</form>
				<form action="${pageContext.request.contextPath}/editProduct" method="post">
					<input type="hidden" name="id" value="<c:out value='${item.getItemID()}'/>"/>
					<input type="submit" name="Edit" value="Edit">
				</form>
				<form action="${pageContext.request.contextPath}/deleteProduct" method="post">
					<input type="hidden" name="id" value="<c:out value='${item.getItemID()}'/>"/>
					<input type="submit" name="Delete" value="Delete">
				</form>
				</td>
			</tr>
		</c:forEach>
	</table>
	<a href="${pageContext.request.contextPath}/product.jsp">Add Product</a>
</div>
<div>Listed Products:<br/>
	<table border="0" cellpadding="5">
		<tr>
			<th>Product Name</th>
			<th>Price</th>
			<th>Minimum Purchase</th>
			<th>Listed Date</th>
			<th>Start Date</th>
			<th>End Date</th>
			<th>Actions</th>
		</tr>
		<c:forEach var="listing" items="${sellerList}">
			<tr>
				<td><c:out value="${listing.getProdName()}" /></td>
				<td><c:out value="${listing.getBaseCost()}" /></td>
				<td><c:out value="${listing.getMinAmount()}" /></td>
				<td><c:out value="${listing.getListDate()}" /></td>
				<td><c:out value="${listing.getEffectiveDate()}" /></td>
				<td><c:out value="${listing.getTerminationDate()}" /></td>
				<td>
				<form action="${pageContext.request.contextPath}/editListing" method="post">
					<input type="hidden" name="id" value="<c:out value='${listing.getListID()}'/>"/>
					<input type="submit" name="Edit" value="Edit">
				</form>
				<form action="${pageContext.request.contextPath}/deleteListing" method="post">
					<input type="hidden" name="id" value="<c:out value='${listing.getListID()}'/>"/>
					<input type="submit" name="Delete" value="Delete">
				</form>
				<%-- 
					<a href="${pageContext.request.contextPath}/editListing?id=<c:out value='${Listing.getListID()}' />">Edit</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
				     <a href="${pageContext.request.contextPath}/deleteListing?id=<c:out value='${Listing.getListID()}' />">Delete</a> --%>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>

<div>Bids for Listings:<br/>
	<table border="0" cellpadding="5">
		<tr>
			<th>Buyer Name</th>
			<th>Product Name</th>
			<th>Posted Date</th>
			<th>Quantity</th>
			<th>Price</th>
			<th>Actions</th>
		</tr>
		<c:forEach var="bid" items="${sellerBidList}">
			<tr>
				<td><c:out value="${bid.getBuyer_name()}" /></td>
				<td><c:out value="${bid.getItem_name()}" /></td>
				<td><c:out value="${bid.getPostedDate()}" /></td>
				<td><c:out value="${bid.getQuantity()}" /></td>
				<td><c:out value="${bid.getProposedPrice()}" /></td>
				<td>
				<form action="${pageContext.request.contextPath}/accpetBid" method="post">
					<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
					<input type="submit" name="Accept" value="Accpet">
				</form>
				<form action="${pageContext.request.contextPath}/rejectBid" method="post">
					<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
					<input type="submit" name="Reject" value="Reject">
				</form>

<%-- 
<a href="${pageContext.request.contextPath}/accpetBid?id=<c:out value='${bid.getBidID()}' />">Accept</a>
	                        &nbsp;&nbsp;&nbsp;&nbsp;
				     <a href="${pageContext.request.contextPath}/rejectBid?id=<c:out value='${bid.getBidID()}' />">Reject</a> --%>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
</div>
</c:if>
<c:if test="${user.getBuyerFlag()}"><br><%-- only try and reference buyer information if the user is a buyer --%>
<div>Buyer information:<br/>
<div>Current Bids:<br/>
	<table border="0" cellpadding="5">
		<tr>
			<th>Seller Name</th>
			<th>Product Name</th>
			<th>Posted Date</th>
			<th>Quantity</th>
			<th>Price</th>
			<th>Actions</th>
		</tr>
		<c:forEach var="bid" items="${buyerBidList}">
			<tr>
				<td><c:out value="${bid.getSeller_name()}" /></td>
				<td><c:out value="${bid.getItem_name()}" /></td>
				<td><c:out value="${bid.getPostedDate()}" /></td>
				<td><c:out value="${bid.getQuantity()}" /></td>
				<td><c:out value="${bid.getProposedPrice()}" /></td>
				<td>
				<form action="${pageContext.request.contextPath}/editBid" method="post">
					<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
					<input type="submit" name="Edit" value="Edit">
				</form>
				<form action="${pageContext.request.contextPath}/deleteBid" method="post">
					<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
					<input type="submit" name="Delete" value="Delete">
				</form>
<%-- <a href="${pageContext.request.contextPath}/editBid?id=<c:out value='${bid.getBidID()}' />">Edit</a>
	                        &nbsp;&nbsp;&nbsp;&nbsp;
				     <a href="${pageContext.request.contextPath}/deleteBid?id=<c:out value='${bid.getBidID()}' />">Delete</a>--%></td>
			</tr>
		</c:forEach>
	</table>
</div>
<div>Available Listings:<br/>
	<table border="0" cellpadding="5">
		<tr>
			<th>Product Name</th>
			<th>Price</th>
			<th>Minimum Purchase</th>
			<th>End Date</th>
			<th>Actions</th>
		</tr>
		<c:forEach var="listing" items="${buyerList}">
			<tr>
				<td><c:out value="${listing.getProdName()}" /></td>
				<td><c:out value="${listing.getBaseCost()}" /></td>
				<td><c:out value="${listing.getMinAmount()}" /></td>
				<td><c:out value="${listing.getTerminationDate()}" /></td>
				<td>
				<form action="${pageContext.request.contextPath}/newBid" method="post">
					<input type="hidden" name="id" value="<c:out value='${listing.getListID()}'/>"/>
					<input type="submit" name="MakeBid" value="Make Bid">
				</form>
<%--<a href="${pageContext.request.contextPath}/newBid?id=<c:out value='${Listing.getListID()}' />">Make Bid</a></td> --%>
			</tr>
		</c:forEach>
	</table>
</div>
</div>
</c:if>

</body>
</html>