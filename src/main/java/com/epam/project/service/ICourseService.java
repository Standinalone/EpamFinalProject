package com.epam.project.service;

import java.util.List;

import com.epam.project.dto.CourseHomePageDto;

public interface ICourseService {
	List<CourseHomePageDto> getCoursesInfoDTOFromTo(int limit, int offset);

	int getCoursesCount();

	int getStudentsCountById(int id);
}
