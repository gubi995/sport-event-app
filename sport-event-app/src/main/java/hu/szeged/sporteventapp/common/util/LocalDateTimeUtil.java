package hu.szeged.sporteventapp.common.util;

import java.time.LocalDateTime;

public class LocalDateTimeUtil {

	public static boolean isFutureDate(LocalDateTime time) {
		LocalDateTime now = LocalDateTime.now();
		return time.isAfter(now);
	}

	public static boolean isStartDateBeforeEndDate(LocalDateTime start, LocalDateTime end) {
		return start.isBefore(end);
	}
}
