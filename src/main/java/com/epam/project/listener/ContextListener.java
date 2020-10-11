package com.epam.project.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.constants.Constants;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;

/**
 * Main listener. Used to configure i18n, encoding systems and establish
 * connection to the database
 *
 */
public class ContextListener implements ServletContextListener {

	private static final Logger log = LoggerFactory.getLogger(ContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.debug("Servlet context initialization starts");

		ServletContext servletContext = event.getServletContext();
		initI18N(servletContext);
		initDb();

		log.debug("Servlet context initialization finished");
	}

	/**
	 * Tries to establish connection to the database
	 * 
	 * @throws DatabaseNotSupportedException
	 */
	private void initDb() {
		log.debug("DB initialization starts");
		// Tries to get connection
		DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
		DaoFactory dao = null;
		try {
			dao = DaoFactory.getDaoFactory(db);
			dao.open();
			log.info("Test connection established");
		} catch (SQLException | DatabaseNotSupportedException e) {
			log.error("Initializing db problem", e.getMessage());
		} finally {
			if (dao != null) {
				dao.close();
				log.info("Test connection closed");
			}
		}
		log.debug("DB initialization finished");
	}

	/**
	 * Initializes i18n subsystem.
	 */
	private void initI18N(ServletContext servletContext) {
		log.debug("I18N subsystem initialization started");

		String localesValue = servletContext.getInitParameter("locales");
		if (localesValue == null || localesValue.isEmpty()) {
			log.warn("'locales' init parameter is empty!");
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

}