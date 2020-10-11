package com.epam.project.constants;

/**
 * Class for storing all application constants
 *
 */
public class Constants {

	/**
	 * Settings
	 */
	public static final String DATABASE = "MYSQL";
	public static final int PAGE_SIZE_COMMON = 4;

	// Url mapping for the front controller
	public static final String PARAM_COMMAND = "command";

	// Token expiration days
	public static final int EXPIRATION_DAYS = 15;

	/**
	 * Pages
	 */
	// Redirect
	public static final String COMMAND__HOME = "?command=HOME_PAGE";
	public static final String COMMAND__LOGIN = "?command=LOGIN_PAGE";
	public static final String COMMAND__ERROR = "?command=ERROR_PAGE";
	public static final String COMMAND__PROFILE = "?command=PROFILE_PAGE";
	public static final String COMMAND__MANAGE_STUDENTS = "?command=MANAGE_STUDENTS_PAGE";
	public static final String COMMAND__SUCCESS = "?command=SUCCESS_PAGE";
	public static final String COMMAND__ADD_EDIT_COURSE = "?command=ADD_EDIT_COURSE_PAGE";
	public static final String COMMAND__REGISTER = "?command=REGISTER_PAGE";
	public static final String COMMAND__MANAGE_COURSES = "?command=MANAGE_COURSES_PAGE";
	public static final String COMMAND__EDIT_LECTURER = "?command=EDIT_LECTURER_PAGE";
	public static final String COMMAND__ADD_LECTURER = "?command=ADD_LECTURER_PAGE";
	public static final String COMMAND_LECTURER_PANEL_PAGE = "?command=LECTURER_PANEL_PAGE";

	// Paths for all users
	public static final String PAGE__ERROR = "/views/error.jsp";
	public static final String PAGE__HOME = "/views/home.jsp";
	public static final String PAGE__COURSES = "/views/courses.jsp";
	public static final String PAGE__LOGIN = "/views/login.jsp";
	public static final String PAGE__REGISTER = "/views/register.jsp";
	public static final String PAGE__SUCCESS = "/views/success.jsp";
	
	// Paths for registered users
	public static final String PAGE__PROFILE = "/views/user/profile.jsp";

	// Paths for lecturers
	public static final String PAGE__MANAGE_JOURNAL = "/views/lecturer/manageJournal.jsp";
	public static final String PAGE__MY_COURSES = "/views/lecturer/mycourses.jsp";

	// Pages for admins
	public static final String PAGE__ADD_LECTURER = "/views/admin/addLecturer.jsp";
	public static final String PAGE__ADD_COURSE = "/views/admin/addEditCourse.jsp";
	public static final String PAGE__MANAGE_COURSES = "/views/admin/manageCourses.jsp";
	public static final String PAGE__MANAGE_STUDENTS = "/views/admin/manageStudents.jsp";
	public static final String PAGE__EDIT_USER = "/views/admin/editUser.jsp";

	// Regex
	public static final String REGEX__USERNAME = "\\p{L}{4,20}";
	// Between 5 and 10 characters, at least one letter and one number
	public static final String REGEX__PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,10}$";
	public static final String REGEX__EMAIL = "\\S+@\\S+\\.\\S+";
	public static final String REGEX__NAME = "\\p{L}{4,20}";

}
