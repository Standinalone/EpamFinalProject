package com.epam.project.constants;

public class Constants {

	public static final String DATABASE = "MYSQL";
	public static final String PROPS_FILE = "app";
	public static final String PROPS_FILE_MESSAGES = "messages";
	public static final int PAGE_SIZE = 2;

	// Url mapping for the front controller
	public static final String PARAM_COMMAND = "command";

	// Redirect
	public static final String PAGE_HOME = "?command=HOME_PAGE";
	public static final String PAGE_LOGIN = "?command=LOGIN_PAGE";
	public static final String PAGE_ERROR = "?command=ERROR_PAGE";
	public static final String PAGE_PROFILE = "?command=PROFILE_PAGE";
	public static final String PAGE_MANAGE_STUDENTS = "?command=MANAGE_STUDENTS_PAGE";

	// Paths for all users
	public static final String PATH_ERROR_PAGE = "/views/error.jsp";
	public static final String PATH_HOMEPAGE = "/views/home.jsp";
	public static final String PATH_COURSES_PAGE = "/views/courses.jsp";
	public static final String PATH_PROFILE_PAGE = "/views/profile.jsp";
	public static final String PATH_LOGIN_PAGE = "/views/login.jsp";
	public static final String PATH_REGISTER_PAGE = "/views/register.jsp";

	// Paths for lecturers
	public static final String PATH_MANAGE_JOURNAL_PAGE = "/views/manageJournal.jsp";
	
	// Paths for admins
	public static final String PATH_ADD_LECTURER_PAGE = "/views/addLecturer.jsp";
	public static final String PATH_ADD_COURSE_PAGE = "/views/addCourse.jsp";
	public static final String PATH_MANAGE_COURSES_PAGE = "/views/manageCourses.jsp";
	public static final String PATH_MANAGE_STUDENTS_PAGE = "/views/manageStudents.jsp";
	public static final String PATH_ADMINISTRATION_PAGE = "/views/administration.jsp";
	
	// Regex
	public static final String REGEX_USERNAME = "\\w{4,20}";
	public static final String REGEX_PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,10}$";



	
	
}
