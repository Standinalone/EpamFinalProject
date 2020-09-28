package com.epam.project.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.constants.Constants;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;

public class CommandAccessFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(CommandAccessFilter.class);

	// commands access
	private static Map<RoleEnum, List<String>> accessMap = new HashMap<RoleEnum, List<String>>();
	private static List<String> commons = new ArrayList<String>();
	private static List<String> outOfControl = new ArrayList<String>();

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("Filter starts");

//		if (accessAllowed(request)) {
//			log.debug("Filter finished");
//			chain.doFilter(request, response);
//		} else {
//			try {
//				((HttpServletResponse) response).sendRedirect(Constants.COMMAND__LOGIN);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		chain.doFilter(request, response);
	}

	private boolean accessAllowed(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String commandName = request.getParameter("command");
		if (commandName == null || commandName.isEmpty())
			return false;

		if (outOfControl.contains(commandName))
			return true;

		HttpSession session = httpRequest.getSession(false);
		if (session == null)
			return false;

		User user = (User) session.getAttribute("user");
		if (user == null)
			return false;

		RoleEnum userRole = user.getRole();
		if (userRole == null)
			return false;

		return accessMap.get(userRole).contains(commandName) || commons.contains(commandName);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		log.debug("Filter initialization starts");

		// roles
		accessMap.put(RoleEnum.ADMIN, asList(fConfig.getInitParameter("admin")));
		accessMap.put(RoleEnum.USER, asList(fConfig.getInitParameter("user")));
		accessMap.put(RoleEnum.LECTURER, asList(fConfig.getInitParameter("lecturer")));
		log.trace("Access map --> {}", accessMap);

		// commons
		commons = asList(fConfig.getInitParameter("common"));
		log.trace("Common commands --> {}", commons);

		// out of control
		outOfControl = asList(fConfig.getInitParameter("out-of-control"));
		log.trace("Out of control commands --> {}", outOfControl);

		log.debug("Filter initialization finished");
	}

	/**
	 * Extracts parameter values from string.
	 * 
	 * @param str parameter values string.
	 * @return list of parameter values.
	 */
	private List<String> asList(String str) {
		List<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(str);
		while (st.hasMoreTokens())
			list.add(st.nextToken());
		return list;
	}

}