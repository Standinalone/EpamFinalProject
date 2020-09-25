<%@ page import="com.epam.project.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<c:set value="${ sessionScope.get(\"successMessage\") }" var="successMessage" scope="page" />
<c:set value="Success" var="title" />

<!-- Header -->
<%@include file='../jsp/header.jsp'%>

<!-- Body -->

<c:if test="${not empty successMessage }">
	<div class="alert alert-info">${ successMessage }</div>
</c:if>

<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>