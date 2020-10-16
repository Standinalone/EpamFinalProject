package com.epam.project.util;

import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.exceptions.DBException;

/**
 * A wrapper around BiFunction interface to manage thrown DBException
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface IGetListFunction<T, U, R> extends BiFunction<T, U, R>{
	Logger log = LoggerFactory.getLogger(IGetListFunction.class);
	@Override
	default R apply(T limit, U offset) {
		try {
			return getList(limit, offset);
		}
		catch(DBException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
	R getList(T limit, U offset ) throws DBException;
	
}
