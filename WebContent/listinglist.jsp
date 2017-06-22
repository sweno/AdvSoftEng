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
			<div><h1>Listed Products:</h1><br/>
				<table class="display_table">
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
								<input type="submit" class="btn btn-primary" name="Edit" value="Edit">
							</form>
							<form action="${pageContext.request.contextPath}/deleteListing" method="post">
								<input type="hidden" name="id" value="<c:out value='${listing.getListID()}'/>"/>
								<input type="submit" class="btn btn-danger" name="Delete" value="Delete">
							</form>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<div><h1>Bids for Listings:</h1><br/>
				<table class="display_table">
					<tr>
						<th>Product Name</th>
						<th>Price</th>
						<th>Quantity</th>
						<th>Posted Date</th>
						<th>Buyer Name</th>
						<th>Actions</th>
					</tr>
					<c:forEach var="bid" items="${sellerBidList}">
						<tr<c:if test="${bid.getBidAccepted()}"> class="bid_accepted"</c:if>>
							<td><c:out value="${bid.getItem_name()}" /></td>
							<td><c:out value="${bid.getProposedPrice()}" /></td>
							<td><c:out value="${bid.getQuantity()}" /></td>
							<td><c:out value="${bid.getPostedDate()}" /></td>
							<td><c:out value="${bid.getBuyer_name()}" /></td>
							<td><c:choose><c:when test="${not bid.getBidAccepted()}">
							<form action="${pageContext.request.contextPath}/accpetBid" method="post">
								<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
								<input type="submit" class="btn btn-primary" name="Accept" value="Accpet">
							</form>
							<form action="${pageContext.request.contextPath}/rejectBid" method="post">
								<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
								<input type="submit" class="btn btn-danger" name="Reject" value="Reject">
							</form>
							</c:when>
							<c:otherwise>Bid Accepted: <c:out value="${bid.getBidAcceptedDate()}" />
							<form action="${pageContext.request.contextPath}/rejectBid" method="post">
								<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
								<input type="submit" class="btn btn-danger" name="Clear" value="Clear">
							</form>
							</c:otherwise></c:choose>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
        </div>
    </div>
</div>
<%@ include file="footer.jspf" %>
</body>
</html>