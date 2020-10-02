<%@ page import="com.epam.project.entity.User"%>
<%@ page import="com.epam.project.constants.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="reg" var="title" />
<c:set value="${ sessionScope.get(\"user\") }" var="user" scope="page" />
<c:set value="${pageContext.request.locale.language}" var="language"
	scope="page" />
<c:set value="${ sessionScope.get(\"successMessage\") }"
	var="successMessage" scope="page" />
<!-- Header -->
<%@include file='../jsp/header.jsp'%>
<script src="${pageContext.servletContext.contextPath}/grades.js"></script>

<!-- Body -->
<fmt:bundle basename="tables">
		<form class="form"
			action="?command=DECLINE_OR_ADD_USERS_TO_COURSE&id=${ param.id }"
			method="post">
				<div class="form-group">
					<label for="status" class="text-info"><fmt:message
							key="manageCourses.status" />:</label><br> <select id="status"
						name="status" class="custom-select nopadding"
						id="inputGroupSelect03">
						<c:forEach items="${  requestScope.get(\"statuses\")  }"
							var="status">
							<option
								<c:if test = "${ status == course.status }">selected</c:if>
								value="${ status }">${ status }</option>

						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<button type="submit" name="submit" class="btn btn-info btn-md"
						value="status">
						<fmt:message key="manageCourses.edit" />
					</button>
				</div>
				<input type="hidden" name="page"
					value="${ pageContext.request.queryString }" />
		</form>
		<h5 class="panel-title pull-left">
			<fmt:message key="journal.inlist" />
		</h5>
		<form id="form"
			action="?command=DECLINE_OR_ADD_USERS_TO_COURSE&id=${ param.id }"
			method="post">
			<div class="table-responsive">
				<table class="table table-hover">
					<tr>
						<th>#</th>
						<th><fmt:message key="users.check" /></th>
						<th><fmt:message key="users.login" /></th>
						<th><fmt:message key="users.name" /></th>
						<th><fmt:message key="users.surname" /></th>
						<th><fmt:message key="users.patronym" /></th>
						<th><fmt:message key="users.email" /></th>
						<th><fmt:message key="users.grade" /></th>
					</tr>
					<c:forEach items="${ requestScope.page1.list }" var="user"
						varStatus="loop">
						<tr <c:if test="${ user.blocked }">class="table-danger"</c:if>>
							<td><c:out
									value="${ requestScope.startIndexEnrolled + loop.index }" /></td>
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
							<td>${ user.login }</td>
							<td>${ user.name }</td>
							<td>${ user.surname }</td>
							<td>${ user.patronym }</td>
							<td>${ user.email }</td>
							<td><input value="${ user.grade }" name="grade-${ user.id }"
								type="number" min="0" max="100" class="form-control grade" /> <%-- 								<input name = "userId" type = "hidden" value = "${ user.id }" /> --%>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<nav aria-label="Page navigation example">
				<ul class="pagination">
					<c:forEach begin="1" end="${ requestScope.totalPagesEnrolled }"
						varStatus="loop">
						<li
							class="page-item <c:if test = "${ param.pagenumEnrolled == loop.index || (empty param.pagenumEnrolled && loop.index == 1) }">active</c:if>"><a
							class="page-link "
							href="?command=MANAGE_STUDENTS_PAGE&pagenum=${ loop.index }"><c:out
									value="${ loop.index }" /></a></li>
					</c:forEach>
				</ul>
			</nav>
			<div class="input-group align-items-center">
				<button type="submit" name="submit" class="btn btn-info btn-md"
					value="remove">
					<fmt:message key="managejournal.remove" />
				</button>
				<button type="submit" name="submit" class="btn btn-info btn-md"
					value="givegrades">
					<fmt:message key="managejournal.givegrade" />
				</button>
				<input type="checkbox" checked type="checkbox" name="sendmail" />
				<fmt:message key="managejournal.sendemail" />
			</div>
			<input type="hidden" name="page"
				value="${ pageContext.request.queryString }" />
		</form>
		<h5 class="panel-title pull-left">
			<fmt:message key="journal.notinlist" />
		</h5>
		<form
			action="?command=DECLINE_OR_ADD_USERS_TO_COURSE&id=${ param.id }"
			method="post">
			<div class="table-responsive">
				<table class="table table-hover">
					<tr>
						<th>#</th>
						<th><fmt:message key="users.check" /></th>
						<th><fmt:message key="users.login" /></th>
						<th><fmt:message key="users.name" /></th>
						<th><fmt:message key="users.surname" /></th>
						<th><fmt:message key="users.patronym" /></th>
						<th><fmt:message key="users.email" /></th>
					</tr>
					<c:forEach items="${ requestScope.page2.list }" var="user"
						varStatus="loop">
						<tr <c:if test="${ user.blocked }">class="table-danger"</c:if>>
							<td><c:out
									value="${ requestScope.startIndexNotEnrolled + loop.index }" /></td>
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
							<td>${ user.login }</td>
							<td>${ user.name }</td>
							<td>${ user.surname }</td>
							<td>${ user.patronym }</td>
							<td>${ user.email }</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<nav aria-label="Page navigation example">
				<ul class="pagination">
					<c:forEach begin="1" end="${ requestScope.totalPagesNotEnrolled }"
						varStatus="loop">
						<li
							class="page-item <c:if test = "${ param.pagenumNotEnrolled == loop.index || (empty param.pagenumEnrolled && loop.index == 1) }">active</c:if>"><a
							class="page-link "
							href="?command=MANAGE_STUDENTS_PAGE&pagenum=${ loop.index }"><c:out
									value="${ loop.index }" /></a></li>
					</c:forEach>
				</ul>
			</nav>
			<div class="input-group align-items-center">
				<button type="submit" name="submit" class="btn btn-info btn-md"
					value="add">
					<fmt:message key="managejournal.add" />
				</button>
				<button type="submit" name="submit" class="btn btn-info btn-md"
					value="decline">
					<fmt:message key="managejournal.decline" />
				</button>
			</div>
			<input type="hidden" name="page"
				value="${ pageContext.request.queryString }" />
		</form>
		<c:if test="${not empty successMessage }">
			<div class="alert alert-info">${ successMessage }</div>
		</c:if>
		<div class="col-md-12">
			<c:forEach items="${ sessionScope.get(\"errors\") }" var="error">
				<div class="alert alert-danger">
					<c:out value="${ error }" />
				</div>
			</c:forEach>
		</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>