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
			<div><h1>Change Password:</h1><br/>
				<form action="${pageContext.request.contextPath}/changePassword" method="post">
					<label for="current">Current Password: </label> <input type="text" name="current" id="current"><br>
					<label for="new1">New Password: </label> <input type="text" name="new1" id="new1"><br>
					<label for="new2">New Password (confirm): </label> <input type="text" name="new2" id="new2"><br>
					<input type="submit" class="btn btn-primary" name="update" value="Update">
				</form>
			</div>
        </div>
    </div>
</div>
<%@ include file="footer.jspf" %>
</body>
</html>