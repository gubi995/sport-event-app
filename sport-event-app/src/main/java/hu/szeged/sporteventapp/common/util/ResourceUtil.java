package hu.szeged.sporteventapp.common.util;

import com.vaadin.server.ThemeResource;

public class ResourceUtil {

	public static String APP = "app/";
	public static String EVENT = "event/";
	public static String USER = "user/";
	public static String VIDEOS = "videos/";
	public static String IMAGES = "images/";

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