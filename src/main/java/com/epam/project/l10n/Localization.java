package com.epam.project.l10n;

import java.util.Locale;
import java.util.ResourceBundle;

import com.epam.project.constants.Constants;

public class Localization {
	public ResourceBundle messages;

	public Localization(Locale locale) {
		messages = ResourceBundle.getBundle(Constants.PROPS_FILE_MESSAGES, locale);
		System.out.println(messages.hashCode());
	}

	public String getMessagesParam(String key) {
		return messages.getString(key);
	}
}
