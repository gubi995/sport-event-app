package hu.szeged.sporteventapp.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.io.Serializable;

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
		VerticalLayout header = new VerticalLayout();
		header.setMargin(false);
		header.setSpacing(false);
		header.addComponent(captionLabel);
		header.setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);
		header.setWidth(100, Unit.PERCENTAGE);
		addComponent(header);
	};

	public abstract void initBody();

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

	}
}
