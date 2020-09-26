package com.epam.project.dao;

import java.sql.SQLException;

import com.epam.project.entity.VerificationToken;

public interface ITokenDAO {
	boolean add(VerificationToken token) throws SQLException;

	VerificationToken findByToken(String token) throws SQLException;
}
