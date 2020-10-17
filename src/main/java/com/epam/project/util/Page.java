package com.epam.project.util;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntSupplier;

import javax.servlet.http.HttpServletRequest;

import com.epam.project.constants.Constants;
import com.epam.project.exceptions.DBException;

/**
 * This util class is used for convenience to eliminate the need of duplicating
 * code for pagination. This object contains a list that is passed to the View
 * layer and represents a range of data
 *
 * @param <T> type of entity objects that the page consists of
 */
public class Page<T> {
	private List<T> list = null;

	@SuppressWarnings("unchecked")
	private <R> void createList(String indexName, String startIndex, String totalPages, HttpServletRequest request,
			BiFunction<Integer, Integer, R> getListFunc, IntSupplier getCountFunc) throws DBException {

		int pageNum = 0;
		if (request.getParameter(indexName) != null) {
			try {
				pageNum = Integer.parseInt(request.getParameter(indexName)) - 1;
			} catch (NumberFormatException e) {
				list = null;
				return;
			}
		}
		int count = getCountFunc.getAsInt();
		if (count == -1)
			throw new DBException("dberror.formPage");

		request.setAttribute(totalPages, Math.ceil((double) count / Constants.PAGE_SIZE_COMMON));
		list = (List<T>) getListFunc.apply(Constants.PAGE_SIZE_COMMON, pageNum * Constants.PAGE_SIZE_COMMON);
		if (list == null)
			throw new DBException("dberror.formPage");

		request.setAttribute(startIndex, pageNum * Constants.PAGE_SIZE_COMMON + 1);

	}

	/**
	 * Creates Page object based on pageNum parameter retrieved from the request
	 * 
	 * @param <R>          type of objects in the page
	 * @param request      incoming request
	 * @param getListFunc  BiFunction responsible for getting records that
	 *                     correspond to conditions and sorting method in a range
	 *                     from the database
	 * @param getCountFunc Supplier responsible for getting count of all records
	 *                     that correspond to conditions
	 * @throws DBException
	 */
//	public <R> Page(HttpServletRequest request, BiFunction<Integer, Integer, R> getListFunc,
//			IntSupplier getCountFunc) {
//		createList("pagenum", "startIndex", "totalPages", request, getListFunc, getCountFunc);
//	}
	public <R> Page(HttpServletRequest request, IGetListFunction<Integer, Integer, R> getListFunc, IGetCountSupplier getCountFunc)
			throws DBException {
		createList("pagenum", "startIndex", "totalPages", request, getListFunc, getCountFunc);
	}

	/**
	 * Creates Page object based on pageNum parameter retrieved from the request.
	 * This constructor may be called when there is a need to create several pages
	 * from different set of data on a single web page
	 * 
	 * @param <R>          type of objects in the page
	 * @param indexName    name of `indexName` parameter used in View layer
	 * @param startIndex   name of `startIndex` parameter used in View layer
	 * @param totalPages   name of `totalPages` parameter used in View layer
	 * @param request      incoming request
	 * @param getListFunc  BiFunction responsible for getting records that
	 *                     correspond to conditions and sorting method in a range
	 *                     from the database
	 * @param getCountFunc Supplier responsible for getting count of all records
	 *                     that correspond to conditions
	 * @throws DBException
	 */
//	public <R> Page(String indexName, String startIndex, String totalPages, HttpServletRequest request,
//			BiFunction<Integer, Integer, R> getListFunc, IntSupplier getCountFunc) {
//		createList(indexName, startIndex, totalPages, request, getListFunc, getCountFunc);
//	}
	public <R> Page(String indexName, String startIndex, String totalPages, HttpServletRequest request,
			IGetListFunction<Integer, Integer, R> getListFunc, IGetCountSupplier getCountFunc) throws DBException {
		createList(indexName, startIndex, totalPages, request, getListFunc, getCountFunc);
	}

	public List<T> getList() {
		return list;
	}
}
