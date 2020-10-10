package com.epam.project.dto;

import java.io.Serializable;

import com.epam.project.entity.Course;

/**
 * Class that consists of a Course object and additional fields
 *
 */
public class CourseProfilePageDto implements Serializable{
	private static final long serialVersionUID = 915470611897557727L;
	private Course course;
	private int grade;
	private String lecturer;
	private String topic;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

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

	public String getLecturer() {
		return lecturer;
	}

	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}
}