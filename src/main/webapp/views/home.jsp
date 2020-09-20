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
<fmt:bundle basename="home">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<h5 class="panel-title pull-left">
							<fmt:message key="home.courselist" />
						</h5>
						<form action="?command=JOIN_COURSES" method="post">
							<table class="table table-hover">
								<tr>
									<c:if test="${ not empty user }">
										<th><fmt:message key="home.check" /></th>
									</c:if>
									<th>#</th>
									<th><fmt:message key="home.students" /></th>
									<th><fmt:message key="home.name" /></th>
									<th><fmt:message key="home.topic" /></th>
									<th><fmt:message key="home.lecturer" /></th>
									<th><fmt:message key="home.startdate" /></th>
									<th><fmt:message key="home.enddate" /></th>
									<th><fmt:message key="home.duration" /></th>
								</tr>
								<c:forEach items="${ requestScope.courses }" var="course"
									varStatus="loop">
									<tr>
										<c:if test="${ not empty user }">
											<td>
												<div class="form-check">
													<input name="courses"
														class="form-check-input position-static" type="checkbox"
														id="blankCheckbox${ loop.index }" value="${ course.course.id }">
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
							<nav aria-label="Page navigation example">
								<ul class="pagination">
									<c:forEach begin="1" end="${ requestScope.totalPages }"
										varStatus="loop">
										<li
											class="page-item <c:if test = "${ param.pagenum == loop.index || (empty param.pagenum && loop.index == 1) }">active</c:if>"><a
											class="page-link "
											href="?command=HOME_PAGE&pagenum=${ loop.index }"><c:out
													value="${ loop.index }" /></a></li>
									</c:forEach>
								</ul>
							</nav>
							<c:if test="${ not empty user }">
								<div class="input-group align-items-center">
									<button type="submit" name="submit" class="btn btn-info btn-md"
										value="block">
										<fmt:message key="home.join" />
									</button>
								</div>
							</c:if>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>