package com.epam.project.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.Topic;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlCourseDAO implements ICourseDAO {
	private static final String SQL_FIND_ALL_COURSES = "SELECT * FROM Courses";
	
	private static final String FIELD_NAME = "name";
	private static final String FIELD_STARTDATE = "start_date";
	private static final String FIELD_ENDDATE = "end_date";
	private static final String FIELD_STATUS = "status";
	private static DaoFactory daoFactory;
	private static MySqlCourseDAO instance;

	private MySqlCourseDAO() {
	}

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public static ICourseDAO getInstance() {
		if (instance == null) {
			instance = new MySqlCourseDAO();
		}
		return instance;
	}

	@Override
	public List<Course> findAllCourses() {
		System.out.println("mysql#findAllCourses");
		List<Course> courses = new ArrayList<>();
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_COURSES);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Course course = new Course();
				LocalDate endDate = rs.getDate(FIELD_ENDDATE).toLocalDate();
				LocalDate startDate = rs.getDate(FIELD_STARTDATE).toLocalDate();

				course.setName(rs.getString(FIELD_NAME));
				course.setStartDate(startDate);
				course.setEndDate(endDate);
				course.setStatus(rs.getBoolean(FIELD_STATUS));
				courses.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return courses;
	}

}
