package com.epam.project.command;
import com.epam.project.command.impl.get.*;
import com.epam.project.command.impl.post.*;

public enum CommandEnum {
	HOME_PAGE(new HomePageCommand()),
	COURSES_PAGE(new CoursesPageCommand()),
	PROFILE_PAGE(new ProfilePageCommand()),
	LOGIN_PAGE(new LoginPageCommand()),
	REGISTER_PAGE(new RegisterPageCommand()),
	MANAGE_STUDENTS_PAGE(new ManageStudentsPageCommand()),
	
	ERROR_PAGE(new ErrorPageCommand()),
	
	LOGIN(new LoginCommand()),
	REGISTER(new RegisterCommand()),
	CHANGE_LANGUAGE(new ChangeLanguageCommand()),
	SIGNOUT(new SignoutCommand()),
	CHANGE_USERS_STATUS(new ChangeUsersStatusCommand()),
	JOIN_COURSES(new JoinCoursesCommand());
			
	private ICommand command;
	
	CommandEnum(ICommand command) {
		this.command = command;
	}
	
	public ICommand getCommand(){
		return command;
	}
}
