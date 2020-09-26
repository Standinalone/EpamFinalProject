package com.epam.project.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class Course implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3174244683045225161L;
	private int id;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private CourseStatusEnum status;
	private int topicId;
	private int lecturerId;
	
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public int getLecturerId() {
		return lecturerId;
	}
	public void setLecturerId(int lecturerId) {
		this.lecturerId = lecturerId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public CourseStatusEnum getStatus() {
		return status;
	}
	public void setStatus(CourseStatusEnum status) {
		this.status = status;
	}
}
