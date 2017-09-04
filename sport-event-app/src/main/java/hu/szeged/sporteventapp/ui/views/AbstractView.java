package hu.szeged.sporteventapp.ui.views;

import java.io.Serializable;

import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
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

	public abstract void initBody();

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

	}
}
