package com.epam.project.i18n;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is used to facilitate getting the ResourceBundle parameters. Its
 * instances are stored in users' sessions and can be recreated to use other
 * locales
 *
 */
public final class Localization implements Serializable {
	private static final long serialVersionUID = -4769669083339226411L;
	private static final String RESOURCES_FILE = "resources";
	private ResourceBundle resources;
	
	/**
	 * @param locale Locale object which the Localization object will be using
	 */
	public Localization(Locale locale) {
		resources = ResourceBundle.getBundle(RESOURCES_FILE, locale);
	}

	/**
	 * @param key key in a ResourceBundle file
	 * @return value for a given key
	 */
	public String getResourcesParam(String key) {
		return resources.getString(key);
	}
}
