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
			<div><h1>Current Bids:</h1><br/>
				<table class="display_table">
					<tr>
						<th>Seller Name</th>
						<th>Product Name</th>
						<th>Posted Date</th>
						<th>Quantity</th>
						<th>Price</th>
						<th>Actions</th>
					</tr>
					<c:forEach var="bid" items="${buyerBidList}">
						<tr<c:if test="${bid.getBidAccepted()}"> class="bid_accepted"</c:if>>
							<td><c:out value="${bid.getSeller_name()}" /></td>
							<td><c:out value="${bid.getItem_name()}" /></td>
							<td><c:out value="${bid.getPostedDate()}" /></td>
							<td><c:out value="${bid.getQuantity()}" /></td>
							<td><c:out value="${bid.getProposedPrice()}" /></td>
							<td>
							<c:choose><c:when test="${not bid.getBidAccepted()}">
							<form action="${pageContext.request.contextPath}/editBid" method="post">
								<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
								<input type="submit" class="btn btn-primary" name="Edit" value="Edit">
							</form>
							<form action="${pageContext.request.contextPath}/deleteBid" method="post">
								<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
								<input type="submit" class="btn btn-danger" name="Delete" value="Delete">
							</form>
							</c:when>
							<c:otherwise>Bid Accepted: <c:out value="${bid.getBidAcceptedDate()}" />
							<form action="${pageContext.request.contextPath}/buyerDetailsBid" method="post">
								<input type="hidden" name="id" value="<c:out value='${bid.getBidID()}'/>"/>
								<input type="submit" class="btn btn-primary" name="Details" value="Details">
							</form>
							<form action="${pageContext.request.contextPath}/deleteBid" method="post">
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