<%@ page import="com.epam.project.entity.User"%>
<%@ page import="com.epam.project.constants.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="reg" var="title" />
<c:set value="${ sessionScope.get(\"user\") }" var="user" scope="page" />
<c:set value="${pageContext.request.locale.language}" var="language"
	scope="page" />
<!-- Header -->
<%@include file='../jsp/header.jsp'%>

<!-- Body -->
<fmt:bundle basename="users">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body"></div>
				</div>
				<br>
				<div class="panel panel-default">
					<div class="panel-body">
						<h5 class="panel-title pull-left">
							<fmt:message key="users.userlist" />
						</h5>
						<form action="?command=CHANGE_USERS_STATUS" method="post">
							<table class="table table-hover">
								<tr>
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
								<c:forEach items="${ requestScope.users }" var="user"
									varStatus="loop">
									<tr <c:if test="${ user.blocked }">class="table-danger"</c:if>>
										<td>
											<div class="form-check">
												<input name="users" class="form-check-input position-static"
													type="checkbox" id="blankCheckbox${ loop.index }"
													value="${ user.id }">
											</div>
										</td>
										<td>${ user.id }</td>
										<td>${ user.login }</td>
										<td>${ user.role }</td>
										<td>${ user.blocked }</td>
										<td>${ user.name }</td>
										<td>${ user.surname }</td>
										<td>${ user.patronym }</td>
										<td>${ user.email }</td>
									</tr>
								</c:forEach>
							</table>
							<nav aria-label="Page navigation example">
								<ul class="pagination">
									<%-- 						    <c:forEach begin="1" end="${ requestScope.totalPages }" varStatus="loop"> --%>
									<%-- 						    	<li class="page-item <c:if test = "${ param.pagenum == loop.index || (empty param.pagenum && loop.index == 1) }">active</c:if>"><a class="page-link " href="?command=PROFILE_PAGE&pagenum=${ loop.index }"><c:out value = "${ loop.index }"/></a></li> --%>
									<%-- 						    </c:forEach> --%>
								</ul>
							</nav>
							<div class="input-group align-items-center">
								<button type="submit" name="submit" class="btn btn-info btn-md"
									value="block"><fmt:message key="users.block"/></button>
								<button type="submit" name="submit" class="btn btn-info btn-md"
									value="unblock"><fmt:message key="users.unblock"/></button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>