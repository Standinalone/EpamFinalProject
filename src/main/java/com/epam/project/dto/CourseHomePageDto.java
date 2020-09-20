package com.epam.project.dto;

import java.time.LocalDate;

import com.epam.project.entity.Course;

public class CourseHomePageDto {

	private Course course;
	private String lecturer;
	private String topic;
	private int students;
	private long duration;

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getLecturer() {
		return lecturer;
	}

	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getStudents() {
		return students;
	}

	public void setStudents(int students) {
		this.students = students;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long l) {
		this.duration = l;
	}

}
