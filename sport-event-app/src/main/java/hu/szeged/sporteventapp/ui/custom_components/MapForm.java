package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.leaflet.util.PointField;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.ui.views.INotifier;

@ViewScope
@SpringComponent
public class MapForm extends VerticalLayout implements INotifier {

	private Boolean readOnly;
	private Button clearButton;
	private PointField pointField;
	private MHorizontalLayout buttonHolder;
	private NoLocationBanner banner;

	private Binder<SportEvent> binder;

	@Autowired
	public MapForm() {
		setSizeFull();
		addStyleName("map-form-v-layout");
		initComponent();
		initBinder();
	}

	private void initComponent() {
		clearButton = new Button(CLEAR);
		clearButton.setStyleName(ValoTheme.BUTTON_DANGER);
		clearButton.addClickListener(c -> clearMapMarker());
		buttonHolder = new MHorizontalLayout().add(clearButton);
		pointField = new PointField(LOCATION);
		pointField.addStyleName("point-field-border");
		banner = new NoLocationBanner();
		binder = new Binder(SportEvent.class);
	}

	private void buildContent(SportEvent sportEvent) {
		removeAllComponents();
		addComponents(buttonHolder);
		if (readOnly) {
			if (sportEvent.getPoint() != null) {
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

	private void initBinder() {
		binder = new Binder(SportEvent.class);
		binder.forField(pointField).bind(SportEvent::getPoint, SportEvent::setPoint);
	}

	private void clearMapMarker() {
		pointField.clear();
	}

	private void setSportEvent(Optional<SportEvent> sportEvent) {
		sportEvent.ifPresent(s -> {
			binder.setBean(s);
			buildContent(s);
		});
	}

	private void setReadOnlyMode(boolean readOnly) {
		this.readOnly = readOnly;
		clearButton.setVisible(!readOnly);
		pointField.setEnabled(!readOnly);
		pointField.setReadOnly(readOnly);
	}

	public void constructMapForm(Optional<SportEvent> sportEvent, boolean readOnly) {
		setReadOnlyMode(readOnly);
		setSportEvent(sportEvent);
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
