			</div>
			<div class="col-sm-3 nopadding">
				<fmt:bundle basename="tables" >
				<div class="list-group list-group-flush">
					<a href="?command=HOME_PAGE" class="list-group-item"><fmt:message key="menu.home" /></a>
					<a href="?command=LOGIN_PAGE" class="list-group-item"><fmt:message key="menu.login" /></a>
					<a href="?command=REGISTER_PAGE" class="list-group-item"><fmt:message key="menu.register" /></a>
					
					<p class = "list-group-item"><b><fmt:message key="menu.users" /></b></p>
					<c:choose>
						<c:when test = "${ not empty user }">
							<a href="?command=PROFILE_PAGE" class="list-group-item"><fmt:message key="menu.myProfile" /></a>
							<form class="list-group-item" action = "?command=SIGNOUT" method = "post"><input class = "btn btn-outline-info" type = "submit" value = "<fmt:message key="menu.exit" />" /></form>
						</c:when>
						<c:otherwise>
							<li class="list-group-item disabled"><fmt:message key="menu.myProfile" /></li>
							<li class="list-group-item disabled"><fmt:message key="menu.exit" /></li>
						</c:otherwise>
					</c:choose>
					
					<p class = "list-group-item"><b><fmt:message key="menu.administration" /></b></p>
					<c:choose>
						<c:when test = "${ user.role == 'ADMIN' }">
							<a href="?command=MANAGE_STUDENTS_PAGE" class="list-group-item"><fmt:message key="menu.manageStudents" /></a>
							<a href="?command=MANAGE_COURSES_PAGE" class="list-group-item"><fmt:message key="menu.manageCourses" /></a>
							<a href="?command=ADD_LECTURER_PAGE" class="list-group-item"><fmt:message key="menu.addlecturer" /></a>
						</c:when>
						<c:otherwise>
							<li class="list-group-item disabled"><fmt:message key="menu.manageStudents" /></li>
							<li class="list-group-item disabled"><fmt:message key="menu.manageCourses" /></li>
							<li class="list-group-item disabled"><fmt:message key="menu.addlecturer" /></li>
						</c:otherwise>
					</c:choose>
					
					<p class = "list-group-item"><b><fmt:message key="menu.lecturers" /></b></p>
					<c:choose>
						<c:when test = "${ user.role == 'LECTURER' }">
							<a href = "?command=MY_COURSES_PAGE" class="list-group-item"><fmt:message key="menu.mycourses" /></a>
							
						</c:when>
						<c:otherwise>
							<li class="list-group-item disabled"><fmt:message key="menu.mycourses" /></li>
							
						</c:otherwise>
					</c:choose>
				</div>
				</fmt:bundle>
			</div>
		</div>
	</div>
	<hr>
	<h:languageList />
	<div class = "text-center"><myTagLib:info param = "info" /></div> 
</body>
</html>
