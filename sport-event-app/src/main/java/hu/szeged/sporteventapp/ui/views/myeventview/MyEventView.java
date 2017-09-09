package hu.szeged.sporteventapp.ui.views.myeventview;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;
import static hu.szeged.sporteventapp.ui.views.eventview.ExploreEventView.generateDataForTest;
import static hu.szeged.sporteventapp.ui.views.myeventview.MyEventView.VIEW_NAME;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.converter.LocalDateTimeConverter;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "manage-my-events")
@SideBarItem(sectionId = Sections.EVENT, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.FILE_PROCESS)
public class MyEventView extends AbstractView implements View, Serializable {

	public static final String VIEW_NAME = "Manage my events";

	private final MyEventPresenter presenter;
	private LocalDateTimeConverter timeConverter;

	private Grid<SportEvent> grid;
	private EventDetailForm eventDetailForm;
	private TextField nameFilter;
	private Button createButton;

	@Autowired
	public MyEventView(MyEventPresenter presenter, LocalDateTimeConverter timeConverter) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.timeConverter = timeConverter;
	}

	@Override
	public void initBody() {
		grid = buildGrid();
		eventDetailForm = new EventDetailForm();
		initFilters();
		initButtons();
		addComponents(
				new MHorizontalLayout().add(nameFilter, createButton)
						.withAlign(createButton, Alignment.BOTTOM_CENTER),
				new MHorizontalLayout().add(grid, eventDetailForm).withFullSize()
						.withExpand(grid, 1));
		eventDetailForm.setVisible(false);
	}

	private Grid<SportEvent> buildGrid() {
		final Grid<SportEvent> grid = new Grid<>(SportEvent.class);
		grid.setSizeFull();

		grid.setItems(generateDataForTest());
		adjustGridCoumns(grid);
		grid.asSingleSelect().addValueChangeListener(e -> {
			if (e.getValue() == null) {
				eventDetailForm.setVisible(false);
			}
			else {
				eventDetailForm.setVisible(true);
			}
		});

		return grid;
	}

	private void initFilters() {
		ListDataProvider<SportEvent> dataProvider = (ListDataProvider<SportEvent>) grid
				.getDataProvider();
		nameFilter = new TextField(NAME);
		nameFilter.addValueChangeListener(e -> updateFilters(dataProvider));
	}

	private void initButtons() {
		createButton = new Button();
		createButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		createButton.setIcon(VaadinIcons.PLUS);
	}

	private void adjustGridCoumns(final Grid<SportEvent> grid) {
		grid.addColumn(SportEvent::getName).setCaption("Event name");
		grid.addColumn(SportEvent::getLocation).setCaption("Location");
		grid.setColumns("name", "location");
		grid.addColumn(sportEvent -> timeConverter.convertLocalDateTimeToString(
				sportEvent.getStartDate(), "yyyy.MM.dd  hh:mm")).setCaption("Start")
				.setWidth(160);
		grid.addColumn(sportEvent -> timeConverter.convertLocalDateTimeToString(
				sportEvent.getEndDate(), "yyyy.MM.dd hh:mm")).setCaption("End")
				.setWidth(160);
		Grid.Column<SportEvent, String> duration = grid.addColumn(sportEvent -> {
			long minutes = sportEvent.getStartDate().until(sportEvent.getEndDate(),
					ChronoUnit.MINUTES);

			return minutes / 60 + ":" + minutes % 60;
		});
		duration.setCaption("Duration(hour:min)");
	}

	private void updateFilters(final ListDataProvider<SportEvent> dataProvider) {
		dataProvider.clearFilters();
		dataProvider.addFilter(sportEvent -> caseInsensitiveContains(sportEvent.getName(),
				nameFilter.getValue()));
	}

	private Boolean caseInsensitiveContains(String where, String what) {
		return where.toLowerCase().contains(what.toLowerCase());
	}

	private class EventDetailForm extends FormLayout {

		TextField nameField;
		TextField locationField;
		TextField maxParticipantField;
		DateTimeField startDateTimeField;
		TextArea detailsArea;
		DateTimeField endDateTimeField;
		NativeSelect<User> comboBox;
		Button deleteParticipantButton;
		Button saveButton;
		Button deleteButton;

		Binder<SportEvent> binder;

		SportEvent sportEvent;

		public EventDetailForm() {
			super();
			setSizeUndefined();
			addStyleName("event_form");
			setMargin(false);
			initComponent();
			addComponents(nameField, locationField, maxParticipantField,
					startDateTimeField, endDateTimeField,
					new MHorizontalLayout(comboBox, deleteParticipantButton).withFullSize()
							.withAlign(deleteParticipantButton, Alignment.BOTTOM_CENTER),
					detailsArea, new MHorizontalLayout(saveButton, deleteButton));
		}

		private void initComponent() {
			nameField = new TextField(NAME);
			locationField = new TextField(LOCATION);
			maxParticipantField = new TextField(MAX_PARTICIPANT);
			startDateTimeField = new DateTimeField(START_DATE);
			endDateTimeField = new DateTimeField(END_DATE);
			detailsArea = new TextArea(DETAILS);
			comboBox = new NativeSelect<>(PARTICIPANTS);
			deleteParticipantButton = new Button();
			deleteParticipantButton.setIcon(VaadinIcons.ERASER);
			deleteParticipantButton.addStyleName(ValoTheme.BUTTON_DANGER);
			saveButton = new Button(SAVE);
			saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			deleteButton = new Button(DELETE);
			deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);

			binder = new Binder<>(SportEvent.class);
		}

		private void setSportEvent() {
			setVisible(true);
		}

		private void save() {
			setVisible(false);
		}

		private void delete() {
			setVisible(false);
		}
	}
}
