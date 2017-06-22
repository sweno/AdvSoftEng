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
			<form action="${pageContext.request.contextPath}/<c:choose><c:when test="${bid != null}">editBid2</c:when><c:otherwise>newBid2</c:otherwise></c:choose>" method="post">
				<c:choose>
					<c:when test="${bid != null}"><h1>Edit Bid</h1><br/>
						<input type="hidden" name="BidID" value="<c:out value="${bid.getBidID()}"/>"/>
						<label for="Product">Product: </label> ${bid.getItem_name()} <span class="note">(due to how bids are processed, products can not be changed at this time.)</span><br/>
						<label for="Seller">Seller: </label> ${bid.getSeller_name()}<br>
						<label for="EndDate">Bid Date: </label> ${bid.getPostedDate()}<br>
						<label for="BaseCost">Cost per unit: </label><input type="text" name="BaseCost" id="BaseCost" value="<c:out value="${bid.getProposedPrice()}"/>"><br>
						<label for="MinPurchase">Quantity Ordered: </label><input type="text" name="MinPurchase" id="MinPurchase" value="<c:out value="${bid.getQuantity()}"/>"><br>
			        	<input type="submit" name="update" value="Update">
					</c:when>
					<c:otherwise><h1>Add New Bid</h1><br/>
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
        </div>
    </div>
</div>
<%@ include file="footer.jspf" %>
</body>
</html>