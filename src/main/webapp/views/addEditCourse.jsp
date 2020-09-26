<%@ page import="com.epam.project.entity.User"%>
<%@ page import="com.epam.project.constants.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="Courses Home Page" var="title" />
<c:set value="${ sessionScope.get(\"user\") }" var="user" scope="page" />
<%-- <c:set value="${ sessionScope.get(\"course\") }" var="user" scope="page" /> --%>
<c:set value="${ requestScope.get(\"course\") }" var="user" scope="page" />
<c:set value="${ pageContext.request.locale.language }" var="language"
	scope="page" />
<c:set value="${ sessionScope.get(\"successMessage\") }"
	var="successMessage" scope="page" />
<!-- Header -->
<%@include file='../jsp/header.jsp'%>

<!-- Body -->
<fmt:bundle basename="manageCourses">
	<div class="container">
		<h3 class="text-center text-info">
			<c:if test="${ empty param.id }">
				<fmt:message key="table.adding" />
			</c:if>
			<c:if test="${ not empty param.id }">
				<fmt:message key="table.editing" />
			</c:if>
		</h3>
		<form class="form" action="?command=ADD_EDIT_COURSE" method="post">
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
						<label for="name" class="text-info"><fmt:message
								key="table.name" />:</label><br> <input type="text" name="name"
							id="name" class="form-control" value="${ course.course.name }">
					</div>
					<div class="form-group">
						<label for="topic" class="text-info"><fmt:message
								key="table.topic" />:</label><br> <select id="topic" name="topic"
							class="custom-select nopadding" id="inputGroupSelect01">

							<c:forEach items="${  requestScope.get(\"topics\")  }"
								var="topic">
								<option
									<c:if test = "${ topic.name == course.topic }">selected</c:if>
									value="${ topic.id }">${ topic.name }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="lecturer" class="text-info"><fmt:message
								key="table.lecturer" />:</label><br> <select id="lecturer"
							name="lecturer" class="custom-select nopadding"
							id="inputGroupSelect02">
							<c:forEach items="${  requestScope.get(\"lecturers\")  }"
								var="lecturer">
								<option
									<c:if test = "${ lecturer.name == course.lecturer }">selected</c:if>
									value="${ lecturer.id }">${ lecturer.name }&nbsp;${ lecturer.surname }
									&nbsp;${ lecturer.patronym }</option>

							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-sm-6">
					<div class="form-group">
						<label for="startdate" class="text-info"><fmt:message
								key="table.startdate" />:</label><br> <input id="startdate"
							name="startdate" class="form-control nopadding" type="date"
							value="${ course.course.startDate }" />
					</div>
					<div class="form-group">
						<label for="enddate" class="text-info"><fmt:message
								key="table.enddate" />:</label><br> <input id="enddate"
							name="enddate" class="form-control nopadding" type="date"
							value="${ course.course.endDate }" />
					</div>
					<div class="form-group">
						<label for="status" class="text-info"><fmt:message
								key="table.status" />:</label><br> <select id="status"
							name="status" class="custom-select nopadding"
							id="inputGroupSelect03">
							<c:forEach items="${  requestScope.get(\"statuses\")  }"
								var="status">
								<option
									<c:if test = "${ status == course.course.status }">selected</c:if>
									value="${ status }">${ status }</option>

							</c:forEach>
						</select>
					</div>
				</div>
			</div>
			<c:if test="${ not empty param.id }">
				<div class="row">
					<div class="col-sm-12">
						<h5>
							Id:
							<c:out value="${ course.course.id }" />
						</h5>
						<h5>
							<fmt:message key="table.students" />
							:
							<c:out value="${ course.students }" />
						</h5>
						<h5>
							<fmt:message key="table.duration" />
							:
							<c:out value="${ course.duration }" />
						</h5>
					</div>
				</div>
			</c:if>
			<div class="form-group">
				<button type="submit" name="submit" class="btn btn-info btn-md"
					value="block">
					<c:if test="${ empty param.id }">
						<fmt:message key="manageCourses.add" />
					</c:if>
					<c:if test="${ not empty param.id }">
						<fmt:message key="manageCourses.edit" />
					</c:if>

				</button>
			</div>
			<c:if test="${ not empty course }">
				<input name="id" type="hidden" value="${ param.id }" />
			</c:if>
		</form>
	</div>
	<div class="col-md-12">
		<c:forEach items="${ sessionScope.get(\"errors\") }" var="error">
			<div class="alert alert-danger">
				<c:out value="${ error }" />
			</div>
		</c:forEach>
		<c:if test="${not empty successMessage }">
			<div class="alert alert-info">${ successMessage }</div>
		</c:if>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>