package hu.szeged.sporteventapp.common.factory;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

	public static Notification createWarningNotification(String message) {
		Notification notification = new Notification(WARNING, message,
				Notification.Type.WARNING_MESSAGE);
		notification.setIcon(VaadinIcons.EXCLAMATION_CIRCLE_O);
		notification.setPosition(Position.BOTTOM_RIGHT);
		return notification;
	}

	public static Notification createErrorNotification(String message) {
		Notification notification = new Notification(ERROR, message,
				Notification.Type.ERROR_MESSAGE);
		notification.setIcon(VaadinIcons.CLOSE_CIRCLE_O);
		notification.setPosition(Position.BOTTOM_RIGHT);
		return notification;
	}

	public static Notification createInfoNotification(String message) {
		Notification notification = new Notification(INFO, message,
				Notification.Type.HUMANIZED_MESSAGE);
		notification.setIcon(VaadinIcons.SMILEY_O);
		notification.setPosition(Position.BOTTOM_RIGHT);
		return notification;
	}
}
