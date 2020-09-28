package com.epam.project.l10n;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Localization implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4769669083339226411L;
	private static final String RESOURCES_FILE = "resources";
	private ResourceBundle resources;

	public Localization(Locale locale) {
		resources = ResourceBundle.getBundle(RESOURCES_FILE, locale);
	}

	public String getResourcesParam(String key) {
		return resources.getString(key);
	}
}
