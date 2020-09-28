<%@ page import="com.epam.project.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="reg" var="title" />

<!-- Header -->
<%@include file='../jsp/header.jsp'%>

<!-- Body -->

<fmt:bundle basename="tables">
	<div id="login">
		<div class="container">
			<h3 class="text-center text-info">
				<fmt:message key="register.register" />
			</h3>
			<form id="login-form" class="form" action="?command=REGISTER"
				method="post">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
							<label for="username" class="text-info"><fmt:message
									key="register.username" />:</label><br> <input type="text"
								name="username" id="username" class="form-control" value="user#">
						</div>
						<div class="form-group">
							<label for="password" class="text-info"><fmt:message
									key="register.password" />:</label><br> <input type="password"
								name="password" id="password" class="form-control" value="asdQwe123">
						</div>
						<div class="form-group">
							<label for="passwordConfirmation" class="text-info"><fmt:message
									key="register.password_confirm" />:</label><br> <input
								type="password" name="passwordConfirmation"
								id="passwordConfirmation" class="form-control"  value="asdQwe123">
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label for="name" class="text-info"><fmt:message
									key="register.name" />:</label><br> <input type="text"
								name="name" id="name" class="form-control"  value="User">
						</div>
						<div class="form-group">
							<label for="surname" class="text-info"><fmt:message
									key="register.surname" />:</label><br> <input type="text"
								name="surname" id="surname" class="form-control" value="Userov">
						</div>
						<div class="form-group">
							<label for="patronym" class="text-info"><fmt:message
									key="register.patronym" />:</label><br> <input type="text"
								name="patronym" id="patronym" class="form-control" value="Userovych">
						</div>
						<div class="form-group">
							<label for="email" class="text-info"><fmt:message
									key="register.email" />:</label><br> <input type="email"
								name="email" id="email" class="form-control" value="standinalone96@gmail.com">
						</div>
					</div>
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
		<div class="col-md-12">
			<c:forEach items="${ sessionScope.get(\"errors\") }" var="error">
				<div class="alert alert-danger">
					<c:out value="${ error }" />
				</div>
			</c:forEach>
		</div>
	</div>


</fmt:bundle>
<!-- Footer -->
<%@include file='../jsp/footer.jsp'%>