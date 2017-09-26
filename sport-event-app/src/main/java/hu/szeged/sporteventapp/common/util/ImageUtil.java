package hu.szeged.sporteventapp.common.util;

import java.io.File;

import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;

public class ImageUtil {

	public static FileResource setImageFileResource(String pictureName) {
        String webAppPath = VaadinService.getCurrent().getBaseDirectory()
                .getAbsolutePath();
        FileResource resource = new FileResource(
                new File(webAppPath + "/images/" + pictureName));
        return resource;
	}

	public static ThemeResource setImageThemeResource(String pictureName) {
		return new ThemeResource("images/" + pictureName);
    }
}