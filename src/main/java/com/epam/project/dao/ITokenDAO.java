package com.epam.project.dao;

import java.sql.SQLException;

import com.epam.project.entity.VerificationToken;

/**
 * Interface for token DAO
 *
 */
public interface ITokenDAO {
	/**
	 * Adds new token to the database
	 * 
	 * @param token VerificationToken object created during registration
	 * @return true if a token was added or false otherwise
	 * @throws SQLException
	 */
	boolean add(VerificationToken token) throws SQLException;

	/**
	 * Finds a token by token string
	 * 
	 * @param token token string
	 * @return VerificationToken object
	 * @throws SQLException
	 */
	VerificationToken findByToken(String token) throws SQLException;
}
