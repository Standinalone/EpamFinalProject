<%@ page import="com.epam.project.entity.User"%>
<%@ page import="com.epam.project.constants.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="Courses Home Page" var="title" />
<c:set value="${ sessionScope.get(\"user\") }" var="user" scope="page" />
<c:set value="${pageContext.request.locale.language}" var="language"
	scope="page" />
<!-- Header -->
<%@include file='/WEB-INF/jspf/header.jspf'%>

<!-- Body -->
<fmt:bundle basename="tables">
	<div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-body">
				<h5 class="panel-title pull-left">
					<fmt:message key="courses.courselist" />
				</h5>
				<form action="?command=JOIN_COURSES" method="post">

					<div class="table-responsive">
						<table class="table table-hover">
							<tr>
								<th>#</th>
								<c:if test="${ not empty user }">
									<th><fmt:message key="courses.check" /></th>
								</c:if>
								<th><fmt:message key="courses.students" /></th>
								<th><fmt:message key="courses.name" /></th>
								<th><fmt:message key="courses.topic" /></th>
								<th><fmt:message key="courses.lecturer" /></th>
								<th><fmt:message key="courses.startdate" /></th>
								<th><fmt:message key="courses.enddate" /></th>
								<th><fmt:message key="courses.duration" /></th>
							</tr>
							<c:forEach items="${ requestScope.page.list }" var="course"
								varStatus="loop">
								<tr>
									<td><c:out value = "${ requestScope.startIndex + loop.index}"/></td>
									<c:if test="${ user.role == 'LECTURER' }">
									<td>
										<a title="<fmt:message key="users.edit" />"
											href="?command=LECTURER_PANEL_PAGE&id=${ course.course.id }"><svg
												width="1em" height="1em" viewBox="0 0 16 16"
												class="bi bi-pencil" fill="currentColor"
												xmlns="http://www.w3.org/2000/svg">
  <path fill-rule="evenodd"
													d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5L13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175l-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z" />
</svg></a></td>
									</c:if>
									<td><c:out value = "${ course.students }"/></td>
									<td><c:out value = "${ course.course.name }"/></td>
									<td><c:out value = "${ course.topic }"/></td>
									<td><c:out value = "${ course.lecturer }"/></td>
									<td><c:out value = "${ course.course.startDate }"/></td>
									<td><c:out value = "${ course.course.endDate }"/></td>
									<td><c:out value = "${ course.duration }"/></td>
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
									href="?command=MY_COURSES_PAGE&pagenum=${ loop.index }"><c:out
											value="${ loop.index }" /></a></li>
							</c:forEach>
						</ul>
					</nav>
					<c:if test="${ not empty user }">
						<div class="input-group align-items-center">
							<button type="submit" name="submit" class="btn btn-info btn-md"
								value="block">
								<fmt:message key="courses.join" />
							</button>
						</div>
					</c:if>
				</form>
			</div>
		</div>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='/WEB-INF/jspf/footer.jspf'%>