package com.epam.project.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet Filter implementation class EncodingFilter. This class is used to set
 * the requests' character encoding to the encoding specified in the filter
 * configuration in web.xml
 */
public class EncodingFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(EncodingFilter.class);
	private String encoding;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.trace("EncodingFilter starts");

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		log.trace("Request uri --> {}", httpRequest.getRequestURI());

		String requestEncoding = request.getCharacterEncoding();
		if (requestEncoding == null) {
			log.trace("Request encoding = null, set encoding --> {}", encoding);
			request.setCharacterEncoding(encoding);
		}

		log.trace("Filter finished");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		log.debug("Filter initialization starts");
		encoding = fConfig.getInitParameter("encoding");
		log.trace("Encoding from web.xml --> {}", encoding);
		log.debug("Filter initialization finished");
	}

}
