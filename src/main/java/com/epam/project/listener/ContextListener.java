package com.epam.project.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextListener implements ServletContextListener {

	private static final Logger log = LoggerFactory.getLogger(ContextListener.class);

	public void contextDestroyed(ServletContextEvent event) {
		log.debug("Servlet context destruction starts");
		// do nothing
		log.debug("Servlet context destruction finished");
	}

	public void contextInitialized(ServletContextEvent event) {
		log.debug("Servlet context initialization starts");

		ServletContext servletContext = event.getServletContext();
//		initCommandContainer();
		initI18N(servletContext);
		initDb();
	
		log.debug("Servlet context initialization finished");
	}

	private void initDb() {

//		throw new RuntimeException();
	}

	/**
	 * Initializes i18n subsystem.
	 */
	private void initI18N(ServletContext servletContext) {
		log.debug("I18N subsystem initialization started");
		
		String localesValue = servletContext.getInitParameter("locales");
		if (localesValue == null || localesValue.isEmpty()) {
			log.warn("'locales' init parameter is empty!");
//			throw new RuntimeException();
		} else {
			List<String> locales = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(localesValue);
			while (st.hasMoreTokens()) {
				String localeName = st.nextToken();
				locales.add(localeName);
			}							
			
			log.debug("Application attribute set: locales --> " + locales);
			servletContext.setAttribute("locales", locales);
		}		
		
		log.debug("I18N subsystem initialization finished");
	}
	
//	private void initCommandContainer() {
//		log.debug("Command container initialization started");
//		
//		// initialize commands container
//		// just load class to JVM
//		try {
//			Class.forName("ua.kharkov.knure.dkolesnikov.st4example.web.command.CommandContainer");
//		} catch (ClassNotFoundException ex) {
//			throw new RuntimeException(ex);
//		}
//		
//		log.debug("Command container initialization finished");
//	}

}