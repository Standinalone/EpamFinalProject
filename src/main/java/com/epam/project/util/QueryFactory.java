package com.epam.project.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This util class is used to form extra query strings when sorting or grouping
 * of entries is required
 *
 */
public final class QueryFactory {
	private static final Logger log = LoggerFactory.getLogger(QueryFactory.class);
	private static final List<String> orders = Arrays.asList("ASC", "DESC");

	private QueryFactory() {
	}

	/**
	 * Creates extra query string from the map of keys, values. Each key represents
	 * a column and each values represents a certain value used in a WHERE clause.
	 * The result of this function may be as follows `lecturer_id = 1 AND course_id
	 * = 2` or `topic_id = 1`. This query is then added to the main SQL query
	 * 
	 * @param map map of keys - column names and values - DB values
	 * @return newly formed condition query from a map
	 */
	public static String formExtraConditionQuery(Map<String, Integer> map) {
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() != 0) {
				sb.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
				prefix = " AND ";
			}
		}
		return sb.toString();
	}

	/**
	 * Create extra query string from the `sort` and `order` parameters retrieved
	 * from the request. The formed query may be as follows ` ORDER BY Topic DESC`.
	 * If received column name doesn't match any of the column names in the map than
	 * nothing is returned. This query is then added to the main SQL query
	 * 
	 * @param COLUMN_NAMES valid column names that exist in the database
	 * @param request      incoming request
	 * @return newly formed orderBy query string
	 */
	public static String formOrderByQuery(final Map<String, String> COLUMN_NAMES, HttpServletRequest request) {
		String columnName = request.getParameter("sort");
		String order = request.getParameter("order");

		if (columnName == null || order == null || !COLUMN_NAMES.keySet().contains(columnName)
				|| !orders.contains(order.toUpperCase())) {
			log.debug("Cannot form orderBy query");
			return "";
		}

		StringBuilder sb = new StringBuilder();
		return sb.append(" ORDER BY ").append(COLUMN_NAMES.get(columnName)).append(' ').append(order).toString();
	}
}
