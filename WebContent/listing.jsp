<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Listings</title>
</head>
<body>
    
<c:if test="${problems != null}">
  	<div>
    <c:forEach items="${problems}" var="problem">
        ${problem}<br/>
    </c:forEach>
    </div>
</c:if>

<form action="${pageContext.request.contextPath}/<c:choose><c:when test="${listing != null}">editListing2</c:when><c:otherwise>newListing2</c:otherwise></c:choose>" method="post">
	<c:choose>
		<c:when test="${listing != null}">Edit Listing<br/>
			<input type="hidden" name="ListingID" value="<c:out value='${listing.getListID()}'/>"/>
			<input type="hidden" name="Product" value="<c:out value='${listing.getProdID()}'/>"/>
			<label for="product">Product: </label> ${listing.getProdName()} <span class="note">due to how listings are processed, products can not be changed at this time.</span><br/>
			<label for="StartDate">Listing Start Date: </label><input type="text" name="StartDate" id="StartDate" value="<c:out value="${listing.getEffectiveDate()}"/>"><br>
			<label for="EndDate">Listing End Date: </label><input type="text" name="EndDate" id="EndDate" value="<c:out value="${listing.getTerminationDate()}"/>"><br>
			<label for="BaseCost">Cost per unit: </label><input type="text" name="BaseCost" id="BaseCost" value="<c:out value="${listing.getBaseCost()}"/>"><br>
			<label for="MinPurchase">Minimum Quantity Ordered: </label><input type="text" name="MinPurchase" id="MinPurchase" value="<c:out value="${listing.getMinAmount()}"/>"><br>
        	<input type="submit" name="update" value="Update">
		</c:when>
		<c:otherwise>Add New Listing<br/>
			<input type="hidden" name="ItemID" value="<c:out value='${item.getItemID()}'/>"/>
			<label for="Product">Product: </label>${item.getName()}<br>
			<label for="StartDate">Listing Start Date: </label><input type="text" name="StartDate" id="StartDate" value="${today}"><br>
			<label for="EndDate">Listing End Date: </label><input type="text" name="EndDate" id="EndDate" value="${nextWeek}"><br>
			<label for="BaseCost">Cost per unit: </label><input type="text" name="BaseCost" id="BaseCost" value=""><br>
			<label for="MinPurchase">Minimum Quantity Ordered: </label><input type="text" name="MinPurchase" id="MinPurchase" value=""><br>
        	<input type="submit" name="Create" value="Create">
		</c:otherwise>
	</c:choose>
</form>

</body>
</html>