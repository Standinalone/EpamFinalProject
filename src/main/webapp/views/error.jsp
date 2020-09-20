<%@ page import="com.epam.project.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="${ sessionScope.get(\"error\") }" var="error" scope="page" />
<c:set value="Error" var="title" />

<!-- Header -->
<%@include file='../jsp/header.jsp'%>

<!-- Body -->

<c:if test="${not empty error }">
	<div class="alert alert-danger">${ error }</div>
</c:if>
<c:if test="${empty error }">
	<div class="alert alert-danger">404</div>
</c:if>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>