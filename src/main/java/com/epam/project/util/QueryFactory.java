package com.epam.project.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.epam.project.entity.CourseStatusEnum;

public final class QueryFactory {
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
				|| !orders.contains(order.toUpperCase()))
			return "";

		StringBuilder sb = new StringBuilder();
		return sb.append(" ORDER BY ").append(COLUMN_NAMES.get(columnName)).append(' ').append(order).toString();
	}

//	public static String formExtraConditionQuery(HttpServletRequest request, String[] params) {
//		Map<String, Object> map = new HashMap<>();
//		for (String param : params) {
//			int i = 0;
//			try {
//				i = Integer.parseInt(request.getParameter(param));
//			} catch (NumberFormatException e) {
//			}
//			map.put(param, i);
//		}
//		int topicId = 0, lecturerId = 0, statusId = 0;
//		try {
//			lecturerId = Integer.parseInt(request.getParameter("lecturer"));
//		} catch (NumberFormatException e) {
//		}
//		try {
//			topicId = Integer.parseInt(request.getParameter("topic"));
//		} catch (NumberFormatException e) {
//		}
//		try {
//			statusId = CourseStatusEnum.valueOf(request.getParameter("status")).ordinal() + 1;
//		} catch (IllegalArgumentException | NullPointerException e) {
//		}
//		
//		map.put("lecturer_id", lecturerId);
//		map.put("topic_id", topicId);
//		map.put("status_id", statusId);
//	}
}
