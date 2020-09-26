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
<fmt:bundle basename="manageCourses">
	<div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-body">
				<h5 class="panel-title pull-left">
					<fmt:message key="manageCourses.courseList" />
				</h5>

				<div class="table-responsive">
					<table class="table table-hover">
						<tr>
							<th><fmt:message key="manageCourses.action" /></th>
							<th>#</th>
							<th><fmt:message key="table.students" /></th>
							<th><fmt:message key="table.name" /></th>
							<th><fmt:message key="table.topic" /></th>
							<th><fmt:message key="table.lecturer" /></th>
							<th><fmt:message key="table.startdate" /></th>
							<th><fmt:message key="table.enddate" /></th>
							<th><fmt:message key="table.duration" /></th>
						</tr>
						<c:forEach items="${ requestScope.courses }" var="course"
							varStatus="loop">
							<tr>
								<td><a
									href="?command=ADD_EDIT_COURSE_PAGE&id=${ course.course.id }"
									class="btn btn-info"><fmt:message key="table.edit" /></a>
									<form action="?command=DELETE_COURSE&id=${ course.course.id }"
										method="post">
										<input type="submit" class="btn btn-info" value = "<fmt:message key="table.delete" />"/>
										
									</form></td>
								<td>${ requestScope.startIndex + loop.index}</td>
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