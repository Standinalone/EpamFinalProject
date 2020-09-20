package com.epam.project.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
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

/**
 * Servlet Filter implementation class LocaleFilter
 */
public class LocaleFilter implements Filter {
	public static final Locale DEFAULT_LOCALE = new Locale("ru_RU");
	public static final List<String> SUPPORTED_LOCALES = Arrays.asList("ru", "en");

	/**
	 * Default constructor.
	 */
	public LocaleFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println(((HttpServletRequest) request).getSession().getAttribute("locale"));
		System.out.println("filter works");
		HttpSession session = ((HttpServletRequest) request).getSession();
		Locale locale = (Locale) session.getAttribute("locale");

		Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		Cookie langaugeCookie = null;
		for (Cookie cookie : cookies) {
			if ("language".equals(cookie.getName())) {
				langaugeCookie = cookie;
				break;
			}
		}

		if (langaugeCookie == null) {
			Enumeration<Locale> locales = request.getLocales();
			while (locales.hasMoreElements()) {
				Locale l = locales.nextElement();
				if (SUPPORTED_LOCALES.contains(l.getLanguage())) {
					session.setAttribute("locale", l);
					break;
				}
			}
			if (!locales.hasMoreElements()) {
				session.setAttribute("locale", DEFAULT_LOCALE);
			}
		} else {
			if (SUPPORTED_LOCALES.contains(langaugeCookie.getValue())) {
				session.setAttribute("locale", new Locale(langaugeCookie.getValue()));
			} else {
				session.setAttribute("locale", DEFAULT_LOCALE);
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
