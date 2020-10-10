package com.epam.project.service;

import com.epam.project.entity.VerificationToken;

/**
 * Interface for VerificationToken service
 *
 */
public interface ITokenService {

	/**
	 * Adds new VerificationToken
	 * 
	 * @param token VerificationToken object
	 * @return true if the VerificationToken was added or false otherwise
	 */
	boolean addToken(VerificationToken token);

	/**
	 * Finds a token by a token string
	 * 
	 * @param token token string
	 * @return VerificationToken if it was found or null otherwise
	 */
	VerificationToken findTokenByToken(String token);
}
