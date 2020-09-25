package com.epam.project.dao;

import com.epam.project.entity.VerificationToken;

public interface ITokenDAO {
	boolean addToken(VerificationToken token);

	VerificationToken getVerificationToken(String token);
}
