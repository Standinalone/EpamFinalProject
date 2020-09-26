package com.epam.project.command;
import com.epam.project.command.impl.get.*;
import com.epam.project.command.impl.post.*;

public enum CommandEnum {
	HOME_PAGE(new HomePageCommand()),
	PROFILE_PAGE(new ProfilePageCommand()),
	LOGIN_PAGE(new LoginPageCommand()),
	REGISTER_PAGE(new RegisterPageCommand()),
	MANAGE_STUDENTS_PAGE(new ManageStudentsPageCommand()),
	MANAGE_COURSES_PAGE(new ManageCoursesPageCommand()),
	SUCCESS_PAGE(new SuccessPageCommand()),
	ADD_EDIT_COURSE_PAGE(new AddEditCoursePageCommand()),
	
	ERROR_PAGE(new ErrorPageCommand()),
	
	LOGIN(new LoginCommand()),
	REGISTER(new RegisterCommand()),
	CHANGE_LANGUAGE(new ChangeLanguageCommand()),
	SIGNOUT(new SignoutCommand()),
	CHANGE_USERS_STATUS(new ChangeUsersStatusCommand()),
	REGISTRATION_CONFIRM(new RegistrationConfirmCommand()),
	ADD_EDIT_COURSE(new AddEditCourseCommand()),
	DELETE_COURSE(new DeleteCourseCommand()),
	JOIN_COURSES(new JoinCoursesCommand());
			
	private ICommand command;
	
	CommandEnum(ICommand command) {
		this.command = command;
	}
	
	public ICommand getCommand(){
		return command;
	}
}
