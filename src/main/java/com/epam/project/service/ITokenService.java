package com.epam.project.service;

import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DBTokenException;

/**
 * Interface for VerificationToken service
 *
 */
public interface ITokenService {

	/**
	 * Adds new VerificationToken
	 * 
	 * @param token VerificationToken object
	 * @throws DBTokenException if SQLException occurred
	 */
	void addToken(VerificationToken token) throws DBTokenException;

	/**
	 * Finds a token by a token string
	 * 
	 * @param token token string
	 * @return VerificationToken if it was found
	 * @throws DBTokenException if SQLException occurred or nothing found
	 */
	VerificationToken findTokenByToken(String token) throws DBTokenException;
}
