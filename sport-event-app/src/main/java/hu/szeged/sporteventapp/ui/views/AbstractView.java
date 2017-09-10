package hu.szeged.sporteventapp.ui.views;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.io.Serializable;

import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class AbstractView extends VerticalLayout implements View, Serializable {

	private final Label captionLabel;

	public AbstractView(String headerCaption) {
		captionLabel = new Label();
		captionLabel.setStyleName(ValoTheme.LABEL_H2);
		setSpacing(true);
		setMargin(true);
		initHeader();
		initComponent();
		initBody();
		setCaptionLabelText(headerCaption);
	}

	private void setCaptionLabelText(String text) {
		captionLabel.setValue(text);
	}

	private void initHeader() {
		addComponent(new MVerticalLayout().withMargin(false).withSpacing(false)
				.withWidth(100, Unit.PERCENTAGE)
				.add(captionLabel, Alignment.MIDDLE_CENTER));
	};

	public abstract void initComponent();

	public abstract void initBody();

	public void showWarningNotification(String message) {
		Notification notification = new Notification(WARNING, message,
				Notification.Type.WARNING_MESSAGE);
		notification.setIcon(VaadinIcons.EXCLAMATION_CIRCLE_O);
		notification.setPosition(Position.BOTTOM_RIGHT);
		notification.show(Page.getCurrent());
	}

	public void showErrorNotification(String message) {
		Notification notification = new Notification(ERROR, message,
				Notification.Type.ERROR_MESSAGE);
		notification.setIcon(VaadinIcons.CLOSE_CIRCLE_O);
		notification.setPosition(Position.BOTTOM_RIGHT);
		notification.show(Page.getCurrent());
	}

	public void showInfoNotification(String message) {
		Notification notification = new Notification(INFO, message,
				Notification.Type.HUMANIZED_MESSAGE);
		notification.setIcon(VaadinIcons.SMILEY_O);
		notification.setPosition(Position.BOTTOM_RIGHT);
		notification.show(Page.getCurrent());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
	}
}
