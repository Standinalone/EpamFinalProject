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
	private int topic_id;
	private int lecturer_id;
	
	public int getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}
	public int getLecturer_id() {
		return lecturer_id;
	}
	public void setLecturer_id(int lecturer_id) {
		this.lecturer_id = lecturer_id;
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
