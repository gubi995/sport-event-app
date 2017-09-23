package hu.szeged.sporteventapp.ui.views;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.io.Serializable;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public interface INotifier extends Serializable {
	default void showWarningNotification(String message) {
		Notification notification = new Notification(WARNING, message,
				Notification.Type.WARNING_MESSAGE);
		notification.setIcon(VaadinIcons.EXCLAMATION_CIRCLE_O);
		notification.setPosition(Position.BOTTOM_RIGHT);
		notification.show(Page.getCurrent());
	}

	default void showErrorNotification(String message) {
		Notification notification = new Notification(ERROR, message,
				Notification.Type.ERROR_MESSAGE);
		notification.setIcon(VaadinIcons.CLOSE_CIRCLE_O);
		notification.setPosition(Position.BOTTOM_RIGHT);
		notification.show(Page.getCurrent());
	}

	default void showInfoNotification(String message) {
		Notification notification = new Notification(INFO, message,
				Notification.Type.HUMANIZED_MESSAGE);
		notification.setIcon(VaadinIcons.SMILEY_O);
		notification.setPosition(Position.BOTTOM_RIGHT);
		notification.show(Page.getCurrent());
	}
}
