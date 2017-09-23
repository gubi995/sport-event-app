package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.Optional;

import org.vaadin.addon.leaflet.util.PointField;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;

public class MapForm extends VerticalLayout {

	private Button saveButton;
	private Boolean readOnly;
	private PointField pointField;
	private NoLocationBanner banner;
	private Binder<SportEvent> binder;

	public MapForm(Optional<SportEvent> sportEvent, Boolean readOnly) {
		this.readOnly = readOnly;
		setSizeFull();
		initComponent();
		buildContent(sportEvent);
		initBinder(sportEvent);
	}

	private void initComponent() {
		saveButton = new Button(SAVE);
		saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		pointField = new PointField(LOCATION);
		banner = new NoLocationBanner();
		binder = new Binder(SportEvent.class);
	}

	private void buildContent(Optional<SportEvent> sportEvent) {
		addComponents(new MHorizontalLayout().withFullWidth().add(saveButton));
		if (readOnly) {
			saveButton.setVisible(false);
			if (sportEvent.isPresent() && sportEvent.get().getPoint() != null) {
				addComponentsAndExpand(pointField);
			}
			else {
				addComponentsAndExpand(banner);
			}
		}
		else {
			addComponentsAndExpand(pointField);
		}
	}

	private void initBinder(Optional<SportEvent> sportEvent) {
		binder = new Binder(SportEvent.class);
		binder.forField(pointField).bind(SportEvent::getPoint, SportEvent::setPoint);
		sportEvent.ifPresent(s -> binder.setBean(s));
	}

	public void showInWindow(UI ui) {
		Window window = new Window(LOCATION + " / " + ROUTE);
		window.setWidth(800, Unit.PIXELS);
		window.setHeight(600, Unit.PIXELS);
		window.setIcon(VaadinIcons.MAP_MARKER);
		window.setResizable(false);
		window.setDraggable(false);
		window.setContent(this);
		window.center();
		ui.addWindow(window);
	}

	private class NoLocationBanner extends VerticalLayout {

		public NoLocationBanner() {
			buildBanner();
		}

		private void buildBanner() {
			MLabel label = new MLabel("There is not given the location of event")
					.withStyleName(ValoTheme.LABEL_H2, ValoTheme.LABEL_COLORED);
			addComponent(label);
			setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		}
	}
}
