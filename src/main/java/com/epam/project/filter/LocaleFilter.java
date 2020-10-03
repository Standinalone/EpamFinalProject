package com.epam.project.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.i18n.Localization;

/**
 * Filter for capturing the language cookie and if it doesn't exist setting the
 * default Locale and Localization objects into the session
 *
 */
public class LocaleFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(LocaleFilter.class);
	private static Locale defaultLocale = null;
	private static final String ATTRIBUTE_LOCALE = "locale";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("Locale filter initialization starts");

		String def = filterConfig.getInitParameter("defaultLocale");
		defaultLocale = new Locale(def);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();

		// If attribute's already set continue chaining

		if (session.getAttribute(ATTRIBUTE_LOCALE) != null) {
			chain.doFilter(request, response);
			return;
		}
		Cookie languageCookie = findLanguageCookie(request);

		setLocaleAndLocalization(request, languageCookie);

		chain.doFilter(request, response);
	}

	/**
	 * If a language cookie doesn't exist checks the request's locales against the
	 * supported locales else checks the locale obtained from the cookie against the
	 * default locales
	 * 
	 * @param request
	 * @param languageCookie
	 */
	private void setLocaleAndLocalization(ServletRequest request, Cookie languageCookie) {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String supportedLocales = request.getServletContext().getInitParameter("locales");
		if (languageCookie == null) {
			Enumeration<Locale> locales = request.getLocales();
			while (locales.hasMoreElements()) {
				Locale l = locales.nextElement();
				if (supportedLocales.contains(l.getLanguage())) {
					session.setAttribute(ATTRIBUTE_LOCALE, l);
					break;
				}
			}
			if (!locales.hasMoreElements()) {
				session.setAttribute(ATTRIBUTE_LOCALE, defaultLocale);
			}
		} else {
			if (supportedLocales != null && supportedLocales.contains(languageCookie.getValue())) {
				session.setAttribute(ATTRIBUTE_LOCALE, new Locale(languageCookie.getValue()));
			} else {
				session.setAttribute(ATTRIBUTE_LOCALE, defaultLocale);
			}
		}
		session.setAttribute("localization", new Localization((Locale)session.getAttribute(ATTRIBUTE_LOCALE)));
	}

	private Cookie findLanguageCookie(ServletRequest request) {
		Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		Cookie languageCookie = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("language".equals(cookie.getName())) {
					languageCookie = cookie;
					break;
				}
			}
		}
		return languageCookie;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
