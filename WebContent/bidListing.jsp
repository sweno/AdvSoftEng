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
			<div><h1>Available Listings:</h1><br/>
				<table class="display_table">
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
								<input type="submit" class="btn btn-primary" name="MakeBid" value="Make Bid">
							</form>
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