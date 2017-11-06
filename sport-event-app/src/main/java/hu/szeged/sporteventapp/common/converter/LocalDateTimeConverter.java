package hu.szeged.sporteventapp.common.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeConverter {

	public String convertLocalDateTimeToString(LocalDateTime localDateTime, String timeFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
		String formatDateTime = localDateTime.format(formatter);
		return formatDateTime;
	}

	public String convertLocalDateTimeToString(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm");
		String formatDateTime = localDateTime.format(formatter);
		return formatDateTime;
	}
}
