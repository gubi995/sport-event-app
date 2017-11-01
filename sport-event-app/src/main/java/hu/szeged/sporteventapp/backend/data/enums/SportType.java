package hu.szeged.sporteventapp.backend.data.enums;

import java.util.Arrays;
import java.util.List;

public class SportType {

	private static final String FOOTBALL = "Football";
	private static final String VOLLEYBALL = "Volleyball";
	private static final String BASKETBALL = "Basketball";
	private static final String GOLF = "Golf";
	private static final String BICYCLE = "Bicycle";
	private static final String HIKE = "Hike";
	private static final String BASEBALL = "Baseball";
	private static final String RUN = "Run";
	private static final String HANDBALL = "Handball";
	private static final String WORKOUT = "Workout";
	private static final String ROWING = "Rowing";
	private static final String SAILING = "Sailing";
	private static final String GO_CART = "Go-cart";
	private static final String PIN_PONG = "Pin pong";
	private static final String SOMETHING_ELSE = "Something else";

	private SportType() {
	}

	public static List<String> getAllSportType() {
		return Arrays.asList(FOOTBALL, VOLLEYBALL, BASKETBALL, GOLF, BICYCLE, HIKE, BASEBALL, RUN, HANDBALL, WORKOUT,
				ROWING, SAILING, GO_CART, PIN_PONG, SOMETHING_ELSE);
	}
}
