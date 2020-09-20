package com.epam.project.Util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class Util {
	
	private Util() {
	}

	public static LocalDate convertToLocalDate(Date date) {
		return LocalDate.from(Instant.ofEpochMilli(date.getTime()));
//		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
