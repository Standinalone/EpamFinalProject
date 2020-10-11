<%@ page import="com.epam.project.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<c:set value="${ sessionScope.get(\"successMessage\") }" var="successMessage" scope="page" />
<c:set value="Success" var="title" />

<!-- Header -->
<%@include file='/WEB-INF/jspf/header.jspf'%>

<!-- Body -->

<c:if test="${not empty successMessage }">
	<div class="alert alert-info"><c:out value = "${ successMessage }"/></div>
</c:if>

<!-- Footer -->
<%@include file='/WEB-INF/jspf/footer.jspf'%>