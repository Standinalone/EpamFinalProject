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
					<fmt:message key="courses.courselist" />
				</h5>
				<form action="?command=JOIN_COURSES" method="post">

					<div class="table-responsive">
						<table class="table table-hover">
							<tr>
								<c:if test="${ not empty user }">
									<th><fmt:message key="courses.check" /></th>
								</c:if>
								<th>#</th>
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
									<c:if test="${ not empty user }">
										<td>
											<div class="form-check">
												<input name="courses"
													class="form-check-input position-static" type="checkbox"
													id="blankCheckbox${ loop.index }"
													value="${ course.course.id }">
											</div>
										</td>
									</c:if>
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
<%@include file='../jsp/footer.jsp'%>