package com.epam.project.service.impl;

import com.epam.project.service.ICourseService;
import com.epam.project.service.ITokenService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class MySqlServiceFactory extends ServiceFactory{
	private static ServiceFactory instance;

	public static ServiceFactory getInstance() {
		if (instance == null) {
			instance = new MySqlServiceFactory();
		}
		return instance;
	}

	@Override
	public IUserService getUserService() {
		return MySqlUserService.getInstance();
	}

	@Override
	public ICourseService getCourseService() {
		return MySqlCourseService.getInstance();
	}

	@Override
	public ITopicService getTopicService() {
		return MySqlTopicService.getInstance();
	}

	@Override
	public ITokenService getTokenService() {
		return MySqlTokenService.getInstance();
	}
	
}
