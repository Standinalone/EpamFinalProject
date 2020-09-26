package com.epam.project.service;

import com.epam.project.entity.VerificationToken;

public interface ITokenService {

	boolean addToken(VerificationToken token);

	VerificationToken findTokenByToken(String token);
}
