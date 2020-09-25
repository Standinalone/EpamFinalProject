<%@ page import="com.epam.project.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<c:set value="${ sessionScope.get(\"error\") }" var="error" scope="page" />
<c:set value="Error" var="title" />
<sql:query var="rs" dataSource="jdbc/epam">
select id, name from users
</sql:query>

<!-- Header -->
<%@include file='../jsp/header.jsp'%>

<!-- Body -->

<c:if test="${not empty error }">
	<div class="alert alert-danger">${ error }</div>
</c:if>
<c:if test="${empty error }">
	<div class="alert alert-danger">404</div>
</c:if>
<c:forEach var="row" items="${rs.rows}">
    Foo ${row.name}<br/>
</c:forEach>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>