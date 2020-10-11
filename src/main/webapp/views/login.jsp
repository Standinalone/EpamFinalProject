<%@ page import="com.epam.project.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="Sign IN" var="title" />

<!-- Header -->
<%@include file='/WEB-INF/jspf/header.jspf'%>

<!-- Body -->

<fmt:bundle basename="tables" >
	<div id="login">
		<div class="container">
			<div id="login-row"
				class="row justify-content-center align-items-center">
				<div id="login-column" class="col-md-12">
					<div id="login-box" class="col-md-12">
						<form id="login-form" class="form" action="?command=LOGIN"
							method="post">
							<h3 class="text-center text-info">
								<fmt:message key="login.login" />
							</h3>
							<div class="form-group">
								<label for="username" class="text-info"><fmt:message
										key="login.username" />:</label><br> <input type="text"
									name="username" id="username" class="form-control"
									">
							</div>
							<div class="form-group">
								<label for="password" class="text-info"><fmt:message
										key="login.password" />:</label><br> <input type="password"
									name="password" id="password" class="form-control"
									>
							</div>
							<div class="form-group">
								<br> <input type="submit" name="submit"
									class="btn btn-info btn-md"
									value="<fmt:message key="login.submit"/>">
							</div>
							<div id="register-link" class="text-right">
								<a href="?command=REGISTER_PAGE" class="text-info"><fmt:message
										key="login.register" /></a>
							</div>
						</form>
					</div>
					<div id="login-box" class="col-md-12">
						<c:forEach items="${ sessionScope.get(\"errors\") }" var="error">
							<div class="alert alert-danger">
								<c:out value="${ error }" />
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
		<div>
			<p class="font-italic">admin - admin</p>
			<p class="font-italic">user - user</p>
			<p class="font-italic">lecturer - lecturer</p>
			<p class="font-italic">blocked_user - blocked_user</p>
			
		</div>
	</div>
</fmt:bundle>
<!-- Footer -->
<%@include file='/WEB-INF/jspf/footer.jspf'%>