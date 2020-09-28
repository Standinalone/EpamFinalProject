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
				<form action="?command=EDIT_LECTURER&id=${ param.id }" method="post">

					<div class="table-responsive">
						<table class="table table-hover" style="font-size: 14px">
							<tr>
								<th>#</th>
								<c:if test="${ not empty user }">
									<th><fmt:message key="courses.check" /></th>
								</c:if>
								<th class="nopadding"><fmt:message key="courses.students" /></th>
								<th class="nopadding"><fmt:message key="courses.name" /></th>
								<th class="nopadding"><fmt:message key="courses.topic" /></th>
								<th class="nopadding"><fmt:message key="courses.lecturer" /></th>
								<th class="nopadding"><fmt:message key="courses.startdate" /></th>
								<th class="nopadding"><fmt:message key="courses.enddate" /></th>
								<th class="nopadding"><fmt:message key="courses.duration" /></th>

								<c:forEach items="${ requestScope.page.list }" var="course"
									varStatus="loop">
									<tr
										<c:if test = "${ course.course.lecturerId == param.id }">class = "table-success"</c:if>>

										<td class="nopadding">${ requestScope.startIndex + loop.index}</td>
										<c:if test="${ not empty user }">
											<td class="nopadding">
												<div class="form-check">
													<input name="courses"
														class="form-check-input position-static" type="checkbox"
														id="blankCheckbox${ loop.index }"
														value="${ course.course.id }">
												</div>
											</td>
										</c:if>
										<td class="nopadding">${ course.students }</td>
										<td class="nopadding">${ course.course.name }</td>
										<td class="nopadding">${ course.topic }</td>
										<td class="nopadding">${ course.lecturer }</td>
										<td class="nopadding">${ course.course.startDate }</td>
										<td class="nopadding">${ course.course.endDate }</td>
										<td class="nopadding">${ course.duration }</td>
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
									href="?command=EDIT_LECTURER_PAGE&pagenum=${ loop.index }&id=${ param.id }"><c:out
											value="${ loop.index }" /></a></li>
							</c:forEach>
						</ul>
					</nav>

					<div class="input-group align-items-center">
						<button type="submit" name="submit" class="btn btn-info btn-md"
							value="appoint">
							<fmt:message key="users.appoint" />
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>