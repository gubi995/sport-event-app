package hu.szeged.sporteventapp.common.util;

import com.vaadin.server.ThemeResource;

public class ResourceUtil {

	private static String APP = "app/";
	private static String EVENT = "event/";
	private static String USER = "user/";
	private static String VIDEOS = "videos/";
	private static String IMAGES = "images/";

	public static ThemeResource setAppIconResource(String pictureName) {
		return new ThemeResource(IMAGES + APP + pictureName);
	}

	public static ThemeResource setUserImageResource(String pictureName) {
		return new ThemeResource(IMAGES + USER + pictureName);
	}

	public static ThemeResource setEventImageResource(String pictureName) {
		return new ThemeResource(IMAGES + EVENT + pictureName);
	}

	public static ThemeResource setVideoResource(String videoName) {
		return new ThemeResource(VIDEOS + videoName);
	}
}