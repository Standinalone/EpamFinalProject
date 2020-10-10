package com.epam.project.i18n;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.exceptions.UnssuportedLocaleException;

/**
 * Class used to get instance of Localization objects to eliminate the need of
 * creating new Localization objects
 *
 */
public final class LocalizationFactory {

	private LocalizationFactory() {
	}

	private static Localization localizationRu = null;
	private static Localization localizationEn = null;

	private static final Logger log = LoggerFactory.getLogger(LocalizationFactory.class);

	public static Localization getLocalization(Locale locale) {
		try {
			switch (locale.getLanguage()) {
			case "ru":
				if (localizationRu == null) {
					localizationRu = new Localization(locale);
				}
				return localizationRu;
			case "en":
				if (localizationEn == null) {
					localizationEn = new Localization(locale);
				}
				return localizationEn;
			default:
				throw new UnssuportedLocaleException(locale.getLanguage() + " isn't supported");
			}
		} catch (UnssuportedLocaleException e) {
			log.error("{} isn't supported", locale.getLanguage());
			if (localizationEn == null) {
				localizationRu = new Localization(locale);
			}
			return localizationEn;
		}

	}
}
