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
            
			<div>Products on File:<br/>
				<table class="display_table" border="0" cellpadding="5">
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
        </div>
    </div>
</div>
<%@ include file="footer.jspf" %>
</body>
</html>