<%@ page import="com.epam.project.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="reg" var="title" />

<!-- Header -->
<%@include file='../jsp/header.jsp'%>

<!-- Body -->

<fmt:bundle basename="labels">
	<div id="login">
		<div class="container">
			<div id="login-row"
				class="row justify-content-center align-items-center">
				<div id="login-column" class="col-md-12">
					<div id="login-box" class="col-md-12">
						<form id="login-form" class="form" action="?command=REGISTER"
							method="post">
							<h3 class="text-center text-info">
								<fmt:message key="register.register" />
							</h3>
							<div class="form-group">
								<label for="username" class="text-info"><fmt:message
										key="register.username" />:</label><br> <input type="text"
									name="username" id="username" class="form-control">
							</div>
							<div class="form-group">
								<label for="password" class="text-info"><fmt:message
										key="register.password" />:</label><br> <input type="password"
									name="password" id="password" class="form-control">
							</div>
							<div class="form-group">
								<label for="passwordConfirmation" class="text-info"><fmt:message
										key="register.password_confirm" />:</label><br> <input
									type="password" name="passwordConfirmation"
									id="passwordConfirmation" class="form-control">
							</div>
							<div class="form-group">
								<br> <input type="submit" name="submit"
									class="btn btn-info btn-md"
									value="<fmt:message key="register.submit"/>">
							</div>
							<div id="register-link" class="text-right">
								<span class="text-secondary"><fmt:message
										key="register.account" /></span> <a href="?command=LOGIN_PAGE"
									class="text-info"><fmt:message key="register.login" /></a>
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
	</div>


</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>