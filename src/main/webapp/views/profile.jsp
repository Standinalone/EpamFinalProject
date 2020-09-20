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
<fmt:bundle basename="profile">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<h3 class="panel-title pull-left">
							<fmt:message key="profile.welcome" />
							<c:out value="${ user.login }" />
						</h3>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-body">
						<div>
							<b><fmt:message key="profile.name" />: </b>
							<c:out value="${ user.name }" />
						</div>
						<div>
							<b><fmt:message key="profile.surname" />: </b>
							<c:out value="${ user.surname }" />
						</div>
						<div>
							<b><fmt:message key="profile.patronym" />: </b>
							<c:out value="${ user.patronym }" />
						</div>
					</div>
				</div>
				<br>
				<div class="panel panel-default">
					<div class="panel-body">
						<h5 class="panel-title pull-left">
							<fmt:message key="profile.mycourses" />
						</h5>
						<table class="table table-hover">
							<tr>
								<th>#</th>
								<th><fmt:message key="table.name" /></th>
								<th><fmt:message key="table.topic" /></th>
								<th><fmt:message key="table.lecturer" /></th>
								<th><fmt:message key="table.startdate" /></th>
								<th><fmt:message key="table.enddate" /></th>
								<th><fmt:message key="table.status" /></th>
								<th><fmt:message key="table.grade" /></th>
							</tr>
							<c:forEach items="${ requestScope.coursesEnrolled }" var="course"
								varStatus="loop">
								<tr
									<c:if test="${ course.status == 'FINISHED' }">class="table-success"</c:if>
									<c:if test="${ course.status == 'NOT_STARTED' }">class="table-danger"</c:if>
									<c:if test="${ course.status == 'IN_PROGRESS' }">class="table-warning"</c:if>>
									<td>${ requestScope.startIndex + loop.index }</td>
									<td>${ course.courseName }</td>
									<td>${ course.topic }</td>
									<td>${ course.lecturer }</td>
									<td>${ course.startDate }</td>
									<td>${ course.endDate }</td>
									<td>${ course.status }</td>
									<td>${ course.grade }</td>
								</tr>
							</c:forEach>
						</table>
						<nav aria-label="Page navigation example">
							<ul class="pagination">
								<c:forEach begin="1" end="${ requestScope.totalPages }"
									varStatus="loop">
									<li
										class="page-item <c:if test = "${ param.pagenum == loop.index || (empty param.pagenum && loop.index == 1) }">active</c:if>"><a
										class="page-link "
										href="?command=PROFILE_PAGE&pagenum=${ loop.index }&pagenumnotenrolled=${ param.pagenumnotenrolled }"><c:out
												value="${ loop.index }" /></a></li>
								</c:forEach>
							</ul>
						</nav>
					</div>
					<div class="panel-body">
						<h5 class="panel-title pull-left">
							<fmt:message key="profile.pending" />
						</h5>
						<table class="table table-hover">
							<tr>
								<th>#</th>
								<th><fmt:message key="table.name" /></th>
								<th><fmt:message key="table.topic" /></th>
								<th><fmt:message key="table.lecturer" /></th>
								<th><fmt:message key="table.startdate" /></th>
								<th><fmt:message key="table.enddate" /></th>
								<th><fmt:message key="table.status" /></th>
							</tr>
							<c:forEach items="${ requestScope.coursesNotEnrolled }"
								var="course" varStatus="loop">
								<tr>
									<td>${ requestScope.startIndex + loop.index }</td>
									<td>${ course.courseName }</td>
									<td>${ course.topic }</td>
									<td>${ course.lecturer }</td>
									<td>${ course.startDate }</td>
									<td>${ course.endDate }</td>
									<td>${ course.status }</td>
								</tr>
							</c:forEach>
						</table>
						<nav aria-label="Page navigation example">
							<ul class="pagination">
								<c:forEach begin="1" end="${ requestScope.totalPagesNotEnrolled }"
									varStatus="loop">
									<li
										class="page-item <c:if test = "${ param.pagenumnotenrolled == loop.index || (empty param.pagenumnotenrolled && loop.index == 1) }">active</c:if>"><a
										class="page-link "
										href="?command=PROFILE_PAGE&pagenum=${ param.pagenum }&pagenumnotenrolled=${ loop.index}"><c:out
												value="${ loop.index }" /></a></li>
								</c:forEach>
							</ul>
						</nav>
					</div>
				</div>
			</div>
		</div>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>