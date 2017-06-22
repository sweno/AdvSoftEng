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
			<form action="${pageContext.request.contextPath}/<c:choose><c:when test="${product != null}">editProduct2</c:when><c:otherwise>newProduct</c:otherwise></c:choose>" method="post">
				<c:choose>
					<c:when test="${product != null}"><h1>Edit Product</h1>
						<input type="hidden" name="itemID" value="<c:out value='${product.getItemID()}'/>"/>
						<label for="name">Name: </label> <input type="text" name="name" id="name" value="<c:out value="${product.getName()}"/>"><br>
						<label for="desc">Description: </label> <input type="text" name="desc" id="desc" value="<c:out value="${product.getDescription()}"/>"><br>
						<label for="keywords">Keywords: </label> <input type="text" name="keywords" id="keywords" value="<c:out value="${product.getKeywords()}"/>"><br>
						<label for="StockQuant">Amount in Stock: </label> <input type="text" name="StockQuant" id="StockQuant" value="<c:out value="${product.getQuantityInStock()}"/>"><br>
						<input type="submit" name="update" value="Update">
					</c:when>
					<c:otherwise><h1>Add New Product</h1>
						<label for="name">Name: </label> <input type="text" name="name" id="name" value=""><br>
						<label for="desc">Description: </label> <input type="text" name="desc" id="desc" value=""><br>
						<label for="keywords">Keywords: </label> <input type="text" name="keywords" id="keywords" value=""><br>
						<label for="StockQuant">Amount in Stock: </label> <input type="text" name="StockQuant" id="StockQuant" value=""><br>
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