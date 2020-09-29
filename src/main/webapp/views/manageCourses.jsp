<%@ page import="com.epam.project.entity.User"%>
<%@ page import="com.epam.project.constants.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="Courses Home Page" var="title" />
<c:set value="${ sessionScope.get(\"user\") }" var="user" scope="page" />
<c:set value="${pageContext.request.locale.language}" var="language"
	scope="page" />
<!-- Header -->
<%@include file='../jsp/header.jsp'%>

<!-- Body -->
<fmt:bundle basename="tables">
	<div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-body">
				<h5 class="panel-title pull-left">
					<fmt:message key="manageCourses.courseList" />
				</h5>

				<div class="table-responsive">
					<table class="table table-hover">
						<tr>
							<th>#</th>
							<th><fmt:message key="manageCourses.action" /></th>
							<th><fmt:message key="manageCourses.students" /></th>
							<th><fmt:message key="manageCourses.name" /></th>
							<th><fmt:message key="manageCourses.topic" /></th>
							<th><fmt:message key="manageCourses.lecturer" /></th>
							<th><fmt:message key="manageCourses.startdate" /></th>
							<th><fmt:message key="manageCourses.enddate" /></th>
							<th><fmt:message key="manageCourses.duration" /></th>
						</tr>
						<c:forEach items="${ requestScope.page.list }" var="course"
							varStatus="loop">
							<tr>
								<td>${ requestScope.startIndex + loop.index}</td>
								<td><div class = "row">
								<div class = "col p-1"><a title="<fmt:message key="users.edit" />"
											href="?command=ADD_EDIT_COURSE_PAGE&id=${ course.course.id }"><svg
												width="1em" height="1em" viewBox="0 0 16 16"
												class="bi bi-pencil" fill="currentColor"
												xmlns="http://www.w3.org/2000/svg">
  <path fill-rule="evenodd"
													d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5L13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175l-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z" />
</svg></a></div>
									<div style = "cursor: pointer" class = "col p-1"><form action="?command=DELETE_COURSE&id=${ course.course.id }"
										method="post">
										<a onclick="this.parentNode.submit();"><svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-x" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
  <path fill-rule="evenodd" d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
</svg></a>
										
									</form></div>
									</div>
									</td>
								<td>${ course.students }</td>
								<td>${ course.course.name }</td>
								<td>${ course.topic }</td>
								<td>${ course.lecturer }</td>
								<td>${ course.course.startDate }</td>
								<td>${ course.course.endDate }</td>
								<td>${ course.duration }</td>
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
								href="?command=MANAGE_COURSES_PAGE&pagenum=${ loop.index }"><c:out
										value="${ loop.index }" /></a></li>
						</c:forEach>
					</ul>
				</nav>
				<div class="input-group align-items-center">
					<a href="?command=ADD_EDIT_COURSE_PAGE" class="btn btn-info btn-md">
						<fmt:message key="manageCourses.add" />
					</a>
				</div>
			</div>
		</div>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>