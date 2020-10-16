<%@ page import="com.epam.project.entity.User"%>
<%@ page import="com.epam.project.constants.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:set value="Courses Home Page" var="title" />
<c:set value="${ sessionScope.get(\"user\") }" var="user" scope="page" />
<c:set value="${pageContext.request.locale.language}" var="language"
	scope="page" />
<c:set value="${ pageContext.request.queryString }" var="queryString" />


<!-- Header -->
<%@include file='/WEB-INF/jspf/header.jspf'%>
<style>
table th {
  cursor: pointer;
}
</style>
<%-- <script src="${pageContext.servletContext.contextPath}/home.js"></script> --%>
<script src="${ context }/js/home.js"></script>
<!-- Body -->





<fmt:bundle basename="tables">
	<fmt:parseNumber var = "lecturerId" type = "number" value = "${lecturer}" />
	<fmt:parseNumber var = "topicId" type = "number" value = "${topic}" />
	<div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-body">
				<h5 class="panel-title pull-left">
					<fmt:message key="courses.courselist" />
				</h5>
				<div class="form-group">
					<div class="row">
						<div class="col-sm-4">
							<label for="lecturer" class="text-info"><fmt:message
									key="courses.lecturers" />:</label><br> <select id="lecturer"
								name="lecturer" class="custom-select nopadding"
								id="lecturers">
								<option selected value="">NOT_SELECTED</option>
								<c:forEach items="${  requestScope.get(\"lecturers\")  }"
									var="lecturer">
<%-- 									<option <c:if test = "${ lecturer.id == param.lecturer }">selected</c:if> value="${ pageContext.request.contextPath }/controller?command=HOME_PAGE&lecturer=${ lecturer.id }&topic=${ param.topic }&status=${ param.status }&sort=${ param.sort }&order=${ param.order }">${ lecturer.name }</option> --%>
									<option <c:if test = "${ lecturer.id == lecturerId }">selected</c:if> value="${ lecturer.id }"><c:out value = "${ lecturer.name } "/></option>

								</c:forEach>
							</select>
						</div>
						<div class="col-sm-4">
							<label for="topic" class="text-info"><fmt:message
									key="courses.topics" />:</label><br> <select id="topic"
								name="topic" class="custom-select nopadding"
								id="inputGroupSelect03">
								<option selected value="">NOT_SELECTED</option>
								<c:forEach items="${  requestScope.get(\"topics\")  }"
									var="topic">
<%-- 									<option <c:if test = "${ topic.id == param.topic }">selected</c:if> value="${ pageContext.request.contextPath }/controller?command=HOME_PAGE&lecturer=${ param.lecturer }&topic=${ topic.id }&status=${ param.status }&sort=${ param.sort }&order=${ param.order }">${ topic.name }</option> --%>
										<option <c:if test = "${ topic.id == topicId }">selected</c:if> value="${ topic.id }"><c:out value = "${ topic.name }"/></option>

								</c:forEach>
							</select>
						</div>
						<div class="col-sm-4">
							<label for="status" class="text-info"><fmt:message
									key="courses.statuses" />:</label><br> <select id="status"
								name="status" class="custom-select nopadding"
								id="inputGroupSelect03">
								<option selected value="">NOT_SELECTED</option>
								<c:forEach items="${  requestScope.get(\"statuses\")  }"
									var="status">
<%-- 									<option <c:if test = "${ status == param.status }">selected</c:if> value="${ pageContext.request.contextPath }/controller?command=HOME_PAGE&lecturer=${ param.lecturer }&topic=${ param.topic }&status=${ status }&sort=${ param.sort }&order=${ param.order }">${ status }</option> --%>
										<option <c:if test = "${ status == param.status }">selected</c:if> value="${ status }"><c:out value = "${ status }"/></option>

								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<form action="?command=JOIN_COURSES" method="post">

					<div class="table-responsive">
						<table id = "mainTable" class="table table-hover">
							<tr>
								<th>#</th>
								<c:if test="${ not empty user }">
									<th><fmt:message key="courses.check" /></th>
								</c:if>
								<th id = "students" ><fmt:message key="courses.students" /></th>
								<th id = "name"><fmt:message key="courses.name" /></th>
								<th id = "topic"><fmt:message key="courses.topic" /></th>
								<th id = "lecturer"><fmt:message key="courses.lecturer" /></th>
								<th id = "startdate"><fmt:message key="courses.startdate" /></th>
								<th id = "enddate"><fmt:message key="courses.enddate" /></th>
								<th id = "duration"><fmt:message key="courses.duration" /></th>
								<th id = "status"><fmt:message key="courses.status" /></th>
							</tr>
							<c:forEach items="${ requestScope.page.list }" var="course"
								varStatus="loop">
								<tr
									<c:if test="${ course.inCourse == true }">class="table-success"</c:if>
								>
									<td><c:out value = "${ requestScope.startIndex + loop.index}" /></td>
									<c:if test="${ not empty user }">
										<td>
											<c:if test = "${ course.inCourse == false }">
												<div class="form-check">
													<input name="courses"
														class="form-check-input position-static" type="checkbox"
														id="blankCheckbox${ loop.index }"
														value="${ course.course.id }">
												</div>
											</c:if>
										</td>
									</c:if>
									<td><c:out value = "${ course.students }" /></td>
									<td><c:out value = "${ course.course.name }"  /></td>
									<td><c:out value = "${ course.topic }"  /></td>
									<td><c:out value = "${ course.lecturer }"  /></td>
									<td><c:out value = "${ course.course.startDate }"  /></td>
									<td><c:out value = "${ course.course.endDate }"  /></td>
									<td><c:out value = "${ course.duration }"  /></td>
									<td><c:out value = "${ course.course.status }"  /></td>
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
									href="?command=HOME_PAGE&pagenum=${ loop.index }&topic=${ param.topic }&lecturer=${ param.lecturer }&sort=${ param.sort }&order=${ param.order }"><c:out
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