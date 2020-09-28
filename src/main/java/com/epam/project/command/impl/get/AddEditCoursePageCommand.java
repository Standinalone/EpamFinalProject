package com.epam.project.command.impl.get;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class AddEditCoursePageCommand implements ICommand {
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static IUserService userService;
	public static ITopicService topicService;
	public static ICourseService courseService;
	public static ServiceFactory serviceFactory;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			topicService = serviceFactory.getTopicService();
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		int courseId;
		String forward = "";
		if (id == null || id.isEmpty()) {
			forward = Constants.PAGE__ADD_COURSE;
		}
		if (id != null && !id.isEmpty()) {
			try {
				courseId = Integer.parseInt(id);
				CourseDto courseDto = courseService.getCourseDtoByCourseId(courseId);
				if (courseDto != null) {
					request.setAttribute("course", courseDto);
					forward = Constants.PAGE__ADD_COURSE;
				}
				else {
					request.setAttribute("error", "Course not found :(");
					return Constants.PAGE__ERROR;
				}
			}
			catch(NumberFormatException e) {
				request.setAttribute("error", "Cannot parse id :(");
				return Constants.PAGE__ERROR;
			}
		}
		request.setAttribute("lecturers", userService.findAllUsersByRole(3));
		request.setAttribute("topics", topicService.findAllTopics());
		request.setAttribute("statuses", CourseStatusEnum.values());
		return forward;
		
		
		/////////////////////////
//		try {
//			request.getRequestDispatcher(Constants.PATH_ADD_COURSE_PAGE).forward(request, response);
//		} catch (IOException | ServletException e) {
//			e.printStackTrace();
//		}
		
		/////////////////////////
//		if (id != null && !id.isEmpty()) {
//			try {
//				courseId = Integer.parseInt(id);
//				CourseDto courseDto = courseService.getCourseDtoByCourseId(courseId);
//				if (courseDto != null) {
//					request.setAttribute("course", courseDto);
////					request.getSession().setAttribute("course", courseDto);
//				}
//				else {
//					try {
//						request.getSession().setAttribute("error", "Course not found :(");
////						response.sendRedirect(Constants.PAGE_ERROR);
//						request.getRequestDispatcher(Constants.PATH_ERROR_PAGE).forward(request, response);
//						return;
//					} catch (IOException | ServletException e1) {
//						e1.printStackTrace();
//						return;
//					}
//				}
//			}
//			catch(NumberFormatException e) {
//				e.printStackTrace();
//				try {
//					request.getSession().setAttribute("error", "Cannot parse id :(");
//					request.getRequestDispatcher(Constants.PATH_ERROR_PAGE).forward(request, response);
////					response.sendRedirect(Constants.PAGE_ERROR);
//					return;
//				} catch (IOException | ServletException e1) {
//					e1.printStackTrace();
//					return;
//				}
//			}
//		}
//		request.getSession().setAttribute("lecturers", userService.findAllUsersByRole(3));
//		request.getSession().setAttribute("topics", topicService.findAllTopics());
//		request.getSession().setAttribute("statuses", CourseStatusEnum.values());
//		try {
//			request.getRequestDispatcher(Constants.PATH_ADD_COURSE_PAGE).forward(request, response);
//		} catch (IOException | ServletException e) {
//			e.printStackTrace();
//		}
	}

}
