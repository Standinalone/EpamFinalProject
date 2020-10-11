<%@ page import="com.epam.project.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="${ requestScope.get(\"error\") }" var="errorForward" scope="page" />
<c:set value="${ sessionScope.get(\"error\") }" var="errorRedirect" scope="page" />
<c:set value="Error" var="title" />

<!-- Header -->
<%@include file='/WEB-INF/jspf/header.jspf'%>

<!-- Body -->

<c:if test="${not empty errorForward }">
	<div class="alert alert-danger"><c:out value = "Forward error: ${ errorForward }"/></div>
</c:if>
<c:if test="${not empty errorRedirect }">
	<div class="alert alert-danger"><c:out value = "Redirect error: ${ errorRedirect }"/></div>
</c:if>
<c:if test="${empty errorForward && empty errorRedirect}">
	<div class="alert alert-danger"><c:out value = "404"/></div>
</c:if>
<!-- Footer -->
<%@include file='/WEB-INF/jspf/footer.jspf'%>