package com.epam.project.entity;

import java.io.Serializable;

/**
 * Class for representing the `Topics` table
 *
 */
public class Topic implements Serializable {
	private static final long serialVersionUID = -3934376450445831710L;
	private int id;
	private String name;

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
}
