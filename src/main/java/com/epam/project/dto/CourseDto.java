package com.epam.project.dto;

import java.io.Serializable;

import com.epam.project.entity.Course;

/**
 * Class that consists of a Course object and additional fields
 *
 */
public class CourseDto implements Serializable {

	private static final long serialVersionUID = 5820873858515453888L;
	private Course course;
	private String lecturer;
	private String topic;
	private int students;
	private long duration;
	
	/**
	 * Field for determining whether a user is in course
	 */
	private boolean inCourse;

	public boolean isInCourse() {
		return inCourse;
	}

	public void setInCourse(boolean inCourse) {
		this.inCourse = inCourse;
	}

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
