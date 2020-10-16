package com.epam.project.util;

import java.util.function.IntSupplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.exceptions.DBException;

/**
 * A wrapper around IntSupplier interface to manage thrown DBException
 *
 */
@FunctionalInterface
public interface IGetCountSupplier extends IntSupplier{
	Logger log = LoggerFactory.getLogger(IGetCountSupplier.class);

	@Override
	default int getAsInt() {
		try {
			return getCount();
		} catch (DBException e) {
			log.error(e.getMessage(), e);
			return -1;
		}
	}
	
	int getCount() throws DBException;
	
}
