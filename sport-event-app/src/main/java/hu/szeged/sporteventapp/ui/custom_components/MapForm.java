package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import org.vaadin.addon.leaflet.util.PointField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;

public class MapForm extends VerticalLayout {

	private SportEvent sportEvent;
	private Button saveButton;
	private PointField location;
	private Binder binder;

	public MapForm() {
		setSizeFull();
		initComponent();
		buildContent();
	}

	private void initComponent() {
		saveButton = new Button(SAVE);
		saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		location = new PointField(LOCATION);
		binder = new Binder(SportEvent.class);
	}

	private void buildContent() {
		addComponents(new MHorizontalLayout().withFullWidth().add(saveButton));
		addComponentsAndExpand(location);
	}


	public void setReadOnly(boolean readOnly) {
		saveButton.setVisible(!readOnly);
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

	public void setSportEvent(SportEvent sportEvent) {
		this.sportEvent = sportEvent;
	}
}
