package com.epam.project.command;

import com.epam.project.command.impl.get.*;
import com.epam.project.command.impl.post.*;

/**
 * Stores command names and the corresponding ICommand implementations. Other
 * commands may be added as the project grows
 *
 */
public enum CommandEnum {
	/**
	 * Get commands
	 */
	HOME_PAGE(new HomePageCommand()), LOGIN_PAGE(new LoginPageCommand()), REGISTER_PAGE(new RegisterPageCommand()),
	SUCCESS_PAGE(new SuccessPageCommand()), ERROR_PAGE(new ErrorPageCommand()),

	// User commands
	PROFILE_PAGE(new ProfilePageCommand()),

	// Admin commands
	MANAGE_STUDENTS_PAGE(new ManageStudentsPageCommand()), MANAGE_COURSES_PAGE(new ManageCoursesPageCommand()),
	ADD_EDIT_COURSE_PAGE(new AddEditCoursePageCommand()), ADD_LECTURER_PAGE(new AddLecturerPageCommand()),
	EDIT_LECTURER_PAGE(new EditLecturerPageCommand()),

	// Lecturer commands
	MY_COURSES_PAGE(new MyCoursesPageCommand()), LECTURER_PANEL_PAGE(new LecturerPanelPageCommand()),

	/**
	 * Post commands
	 */
	LOGIN(new LoginCommand()), REGISTER(new RegisterCommand()), CHANGE_LANGUAGE(new ChangeLanguageCommand()),
	REGISTRATION_CONFIRM(new RegistrationConfirmCommand()),
	// User commands
	SIGNOUT(new SignoutCommand()), JOIN_COURSES(new JoinCoursesCommand()),

	// Admin commands
	EDIT_LECTURER(new EditLecturerCommand()), CHANGE_USERS_STATUS(new ChangeUsersStatusCommand()),
	DELETE_COURSE(new DeleteCourseCommand()), ADD_EDIT_COURSE(new AddEditCourseCommand()),

	// Lecturer commands
	DECLINE_OR_ADD_USERS_TO_COURSE(new LecturerPanelCommand());

	private ICommand command;

	CommandEnum(ICommand command) {
		this.command = command;
	}

	public ICommand getCommand() {
		return command;
	}
}
