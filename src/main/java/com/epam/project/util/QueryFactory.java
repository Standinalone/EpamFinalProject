package com.epam.project.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QueryFactory {
	private static final Logger log = LoggerFactory.getLogger(QueryFactory.class);
	private static final List<String> orders = Arrays.asList("ASC", "DESC");

	private QueryFactory() {
	}

	public static String formExtraConditionQuery(Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if ((Integer) entry.getValue() != 0) {
				sb.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
				prefix = " AND ";
			}
		}
		return sb.toString();
	}

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
