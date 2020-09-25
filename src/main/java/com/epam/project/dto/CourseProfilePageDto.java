package com.epam.project.dto;

import java.time.LocalDate;

import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;

public class CourseProfilePageDto {
	private Course course;
//	private String courseName;
//	private LocalDate startDate;
//	private LocalDate endDate;
	private int grade;
	private CourseStatusEnum status;
	private String lecturer;
	private String topic;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

//	public String getCourseName() {
//		return courseName;
//	}
//
//	public void setCourseName(String courseName) {
//		this.courseName = courseName;
//	}
//
//	public LocalDate getStartDate() {
//		return startDate;
//	}
//
//	public void setStartDate(LocalDate startDate) {
//		this.startDate = startDate;
//	}
//
//	public LocalDate getEndDate() {
//		return endDate;
//	}
//
//	public void setEndDate(LocalDate endDate) {
//		this.endDate = endDate;
//	}

	public int getGrade() {
		return grade;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public CourseStatusEnum getStatus() {
		return status;
	}

	public void setStatus(CourseStatusEnum status) {
		this.status = status;
	}

	public String getLecturer() {
		return lecturer;
	}

	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}

}
