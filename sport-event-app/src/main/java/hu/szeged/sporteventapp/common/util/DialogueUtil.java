package hu.szeged.sporteventapp.common.util;

import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class DialogueUtil {

	public static void showInWindow(UI ui, Component content, String caption, Resource icon, int width, int height) {
		Window window = new Window(caption, content);
		window.setWidth(width, Sizeable.Unit.PIXELS);
		window.setHeight(height, Sizeable.Unit.PIXELS);
		window.setIcon(icon);
		window.setResizable(false);
		window.setDraggable(false);
		window.center();
		ui.addWindow(window);
	}

	public static void showInWindow(UI ui, Component content, String caption, Resource icon) {
		Window window = new Window(caption, content);
		window.setSizeFull();
		window.setIcon(icon);
		window.setResizable(false);
		window.setDraggable(false);
		window.center();
		ui.addWindow(window);
	}
}
