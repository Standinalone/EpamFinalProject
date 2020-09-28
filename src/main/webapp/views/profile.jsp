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
<fmt:bundle basename="tables">
	<div class="container">

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
				<div class="table-responsive">
					<table class="table table-hover">
						<tr>
							<th>#</th>
							<th><fmt:message key="profile.name" /></th>
							<th><fmt:message key="profile.topic" /></th>
							<th><fmt:message key="profile.lecturer" /></th>
							<th><fmt:message key="profile.startdate" /></th>
							<th><fmt:message key="profile.enddate" /></th>
							<th><fmt:message key="profile.status" /></th>
							<th><fmt:message key="profile.grade" /></th>
						</tr>
						<c:forEach items="${ requestScope.page1.list }" var="course"
							varStatus="loop">
							<tr
								<c:if test="${ course.course.status == 'FINISHED' }">class="table-success"</c:if>
								<c:if test="${ course.course.status == 'NOT_STARTED' }">class="table-danger"</c:if>
								<c:if test="${ course.course.status == 'IN_PROGRESS' }">class="table-warning"</c:if>>
								<td>${ requestScope.startIndexEnrolled + loop.index }</td>
								<td>${ course.course.name }</td>
								<td>${ course.topic }</td>
								<td>${ course.lecturer }</td>
								<td>${ course.course.startDate }</td>
								<td>${ course.course.endDate }</td>
								<td>${ course.course.status }</td>
								<td>${ course.grade }</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<nav aria-label="Page navigation example">
					<ul class="pagination">
						<c:forEach begin="1" end="${ requestScope.totalPagesEnrolled }"
							varStatus="loop">
							<li
								class="page-item <c:if test = "${ param.pagenumenrolled == loop.index || (empty param.pagenumenrolled && loop.index == 1) }">active</c:if>"><a
								class="page-link "
								href="?command=PROFILE_PAGE&pagenumenrolled=${ loop.index }<c:if test = "${ not empty param.pagenumnotenrolled }">&pagenumnotenrolled=${ param.pagenumnotenrolled }</c:if>"><c:out
										value="${ loop.index }" /></a></li>
						</c:forEach>
					</ul>
				</nav>
			</div>
			<div class="panel-body">
				<h5 class="panel-title pull-left">
					<fmt:message key="profile.pending" />
				</h5>
				<div class="table-responsive">
					<table class="table table-hover">
						<tr>
							<th>#</th>
							<th><fmt:message key="profile.name" /></th>
							<th><fmt:message key="profile.topic" /></th>
							<th><fmt:message key="profile.lecturer" /></th>
							<th><fmt:message key="profile.startdate" /></th>
							<th><fmt:message key="profile.enddate" /></th>
							<th><fmt:message key="profile.status" /></th>
						</tr>
						<c:forEach items="${ requestScope.page2.list }"
							var="course" varStatus="loop">
							<tr>
								<td>${ requestScope.startIndexNotEnrolled + loop.index }</td>
								<td>${ course.course.name }</td>
								<td>${ course.topic }</td>
								<td>${ course.lecturer }</td>
								<td>${ course.course.startDate }</td>
								<td>${ course.course.endDate }</td>
								<td>${ course.course.status }</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<nav aria-label="Page navigation example">
					<ul class="pagination">
						<c:forEach begin="1" end="${ requestScope.totalPagesNotEnrolled }"
							varStatus="loop">
							<li
								class="page-item <c:if test = "${ param.pagenumnotenrolled == loop.index || (empty param.pagenumnotenrolled && loop.index == 1) }">active</c:if>"><a
								class="page-link "
								href="?command=PROFILE_PAGE<c:if test = "${ not empty param.pagenumenrolled }">&pagenumenrolled=${ param.pagenumenrolled }</c:if>&pagenumnotenrolled=${ loop.index}"><c:out
										value="${ loop.index }" /></a></li>
						</c:forEach>
					</ul>
				</nav>
			</div>
		</div>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>