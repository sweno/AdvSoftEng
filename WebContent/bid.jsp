<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Bids</title>
</head>
<body>

    <c:if test="${problems != null}">
    	<div>
        <c:forEach items="${problems}" var="problem">
            ${problem}<br/>
        </c:forEach>
        </div>
    </c:if>

<form action="${pageContext.request.contextPath}/<c:choose><c:when test="${bid != null}">editBid2</c:when><c:otherwise>newBid2</c:otherwise></c:choose>" method="post">
	<c:choose>
		<c:when test="${bid != null}">Edit Bid<br/>
			<input type="hidden" name="BidID" value="<c:out value="${bid.getBidID()}"/>"/>
			<label for="Product">Product: </label> ${bid.getItem_name()} <span class="note">(due to how bids are processed, products can not be changed at this time.)</span><br/>
			<label for="Seller">Seller: </label> ${bid.getSeller_name()}<br>
			<label for="EndDate">Bid Date: </label> ${bid.getBidDate()}<br>
			<label for="BaseCost">Cost per unit: </label><input type="text" name="BaseCost" id="BaseCost" value="<c:out value="${bid.getBaseCost()}"/>"><br>
			<label for="PurchaseQuant">Quantity Ordered: </label><input type="text" name="PurchaseQuant" id="PurchaseQuant" value="<c:out value="${bid.getQuantity()}"/>"><br>
        	<input type="submit" name="update" value="Update">
		</c:when>
		<c:otherwise>Add New Bid<br/>
			<input type="hidden" name="SellerID" value="<c:out value="${sellerID}"/>"/>
			<input type="hidden" name="ItemID" value="<c:out value="${listing.getProdID()}"/>"/>
			<label for="Product">Product: </label><c:out value="${listing.getProdName()}"/><br>
			<label for="Seller">Seller: </label><c:out value="${sellerName}"/><br>
			<label for="BaseCost">Cost per unit: </label><input type="text" name="BaseCost" id="BaseCost" value="<c:out value="${listing.getBaseCost()}"/>"><br>
			<label for="MinPurchase">Quantity Ordered: </label><input type="text" name="MinPurchase" id="MinPurchase" value="<c:out value="${listing.getMinAmount()}"/>"><br>
        	<input type="submit" name="Create" value="Create">
		</c:otherwise>
	</c:choose>
</form>

</body>
</html>