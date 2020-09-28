package com.epam.project.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import com.epam.project.constants.Constants;

public class Page<T> {
	private List<T> list = new ArrayList<>();

	private <R> void createList(String indexName, String startIndex, String totalPages, HttpServletRequest request,
			BiFunction<Integer, Integer, R> getListFunc, Supplier<Integer> getCountFunc) {

		int pageNum = 0;
		if (request.getParameter(indexName) != null) {
			try {
				pageNum = Integer.parseInt(request.getParameter(indexName)) - 1;
			} catch (NumberFormatException e) {
				list = null;
				return;
			}
		}
		list = (List<T>) getListFunc.apply(Constants.PAGE_SIZE_COMMON, pageNum * Constants.PAGE_SIZE_COMMON);
		request.setAttribute(startIndex, pageNum * Constants.PAGE_SIZE_COMMON + 1);

		int count = getCountFunc.get();
		request.setAttribute(totalPages, Math.ceil((double) count / Constants.PAGE_SIZE_COMMON));

	}

	public <R> Page(HttpServletRequest request, BiFunction<Integer, Integer, R> getListFunc,
			Supplier<Integer> getCountFunc) {
		createList("pagenum", "startIndex", "totalPages", request, getListFunc, getCountFunc);
	}
	
	public <R> Page(String indexName, String startIndex, String totalPages, HttpServletRequest request, BiFunction<Integer, Integer, R> getListFunc,
			Supplier<Integer> getCountFunc) {
		createList(indexName, startIndex, totalPages, request, getListFunc, getCountFunc);
	}

	public List<T> getList() {
		return list;
	}
}
