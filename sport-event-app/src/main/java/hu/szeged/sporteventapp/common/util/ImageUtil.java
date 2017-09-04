package hu.szeged.sporteventapp.common.util;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;

import java.io.File;

public class ImageUtil {

    public static FileResource setImageResource(String pictureName) {
        String webAppPath = VaadinService.getCurrent().getBaseDirectory()
                .getAbsolutePath();
        FileResource resource = new FileResource(
                new File(webAppPath + "/images/" + pictureName));
        return resource;
    }
}