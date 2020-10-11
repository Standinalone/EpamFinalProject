<%@ page import="com.epam.project.entity.User"%>
<%@ page import="com.epam.project.constants.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="reg" var="title" />
<c:set value="${ sessionScope.get(\"user\") }" var="user" scope="page" />
<c:set value="${pageContext.request.locale.language}" var="language"
	scope="page" />
<!-- Header -->
<%@include file='/WEB-INF/jspf/header.jspf'%>

<!-- Body -->
<fmt:bundle basename="tables">
	<div class="container">
		<h5 class="panel-title pull-left">
			<fmt:message key="users.userlist" />
		</h5>
		<form action="?command=CHANGE_USERS_STATUS" method="post">
			<div class="table-responsive">
				<table class="table table-hover">
					<tr>
						<th>#</th>
						<th><fmt:message key="users.check" /></th>
						<th><fmt:message key="users.id" /></th>
						<th><fmt:message key="users.login" /></th>
						<th><fmt:message key="users.role" /></th>
						<th><fmt:message key="users.blocked" /></th>
						<th><fmt:message key="users.name" /></th>
						<th><fmt:message key="users.surname" /></th>
						<th><fmt:message key="users.patronym" /></th>
						<th><fmt:message key="users.email" /></th>
					</tr>
					<c:forEach items="${ requestScope.page.list }" var="user"
						varStatus="loop">
						<tr <c:if test="${ user.blocked }">class="table-danger"</c:if>>
							<td><c:out value = "${ requestScope.startIndex + loop.index }"/></td>
							<td>
								<div class="form-check d-inline">
									<input name="users" class="form-check-input position-static"
										type="checkbox" id="blankCheckbox${ loop.index }"
										value="${ user.id }">
									<c:if test="${ user.role == 'LECTURER' }">
										<a title="<fmt:message key="users.edit" />"
											href="?command=EDIT_LECTURER_PAGE&id=${ user.id }"><svg
												width="1em" height="1em" viewBox="0 0 16 16"
												class="bi bi-pencil" fill="currentColor"
												xmlns="http://www.w3.org/2000/svg">
  <path fill-rule="evenodd"
													d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5L13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175l-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z" />
</svg></a>
									</c:if>
								</div>
							</td>
							<td><c:out value = "${ user.id }"/></td>
							<td><c:out value = "${ user.login }"/></td>
							<td><c:out value = "${ user.role }"/></td>
							<td><c:out value = "${ user.blocked }"/></td>
							<td><c:out value = "${ user.name }"/></td>
							<td><c:out value = "${ user.surname }"/></td>
							<td><c:out value = "${ user.patronym }"/></td>
							<td><c:out value = "${ user.email }"/></td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<nav aria-label="Page navigation example">
				<ul class="pagination">
					<c:forEach begin="1" end="${ requestScope.totalPages }"
						varStatus="loop">
						<li
							class="page-item <c:if test = "${ param.pagenum == loop.index || (empty param.pagenum && loop.index == 1) }">active</c:if>"><a
							class="page-link "
							href="?command=MANAGE_STUDENTS_PAGE&pagenum=${ loop.index }"><c:out
									value="${ loop.index }" /></a></li>
					</c:forEach>
				</ul>
			</nav>
			<div class="input-group align-items-center">
				<button type="submit" name="submit" class="btn btn-info btn-md"
					value="block">
					<fmt:message key="users.block" />
				</button>
				<button type="submit" name="submit" class="btn btn-info btn-md"
					value="unblock">
					<fmt:message key="users.unblock" />
				</button>
			</div>
			<input type="hidden" name="page"
				value="${ pageContext.request.queryString }" />
		</form>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='/WEB-INF/jspf/footer.jspf'%>