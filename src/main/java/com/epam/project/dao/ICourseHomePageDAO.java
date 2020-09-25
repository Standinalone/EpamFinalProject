package com.epam.project.dao;

import java.util.List;

import com.epam.project.dto.CourseHomePageDto;

public interface ICourseHomePageDAO {
	List<CourseHomePageDto> findAllCoursesHomePageFromTo(int limit, int offset);

	CourseHomePageDto findById(int courseId);
}
