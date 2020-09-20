package com.epam.project.service;

import java.util.List;

import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.User;

public interface IUserService {
	List<CourseProfilePageDto> getCoursesDTO(User user);

	int getCoursesDTOCount(User user, boolean b);

	List<CourseProfilePageDto> getNotEnrolledCoursesDTOFromTo(User user, int limit, int offset);

	List<CourseProfilePageDto> getEnrolledCoursesDTOFromTo(User user, int pageSize, int i);

	void blockUser(int id);

	void unblockUser(int id);

	void enrollToCourse(int course_id, int user_id);

}
