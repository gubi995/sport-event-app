package hu.szeged.sporteventapp.ui.views.eventviews;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;
import static hu.szeged.sporteventapp.ui.views.eventviews.ManageEventView.VIEW_NAME;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.converter.LocalDateTimeConverter;
import hu.szeged.sporteventapp.common.factory.MyBeanFactory;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.custom_components.MapForm;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "manage-my-events")
@SideBarItem(sectionId = Sections.EVENT, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.FILE_PROCESS)
public class ManageEventView extends AbstractView {

	public static final String VIEW_NAME = "Manage my events";

	private final ManageEventPresenter presenter;
	private LocalDateTimeConverter timeConverter;
	private final MapForm mapForm;

	private Grid<SportEvent> grid;
	private EventDataForm eventDataForm;
	private TextField nameFilter;
	private Button createButton;

	@Autowired
	public ManageEventView(ManageEventPresenter presenter, LocalDateTimeConverter timeConverter,
						   MapForm mapForm) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.timeConverter = timeConverter;
		this.mapForm = mapForm;
	}

	@Override
	public void initComponent() {
		eventDataForm = new EventDataForm();
		nameFilter = new TextField(NAME);
		createButton = new Button();
	}

	@Override
	public void initBody() {
		grid = buildGrid();
		initButtons();
		addComponentsAndExpand(new MCssLayout().withFullWidth().add(
				new MHorizontalLayout().withSpacing(true).add(nameFilter, createButton)
						.withAlign(createButton, Alignment.BOTTOM_CENTER),
						new MHorizontalLayout().withStyleName("manage-event-h-layout")
								.withSpacing(true).add(grid, eventDataForm).withFullSize()
								.withExpand(grid, 1)));
		eventDataForm.setVisible(false);
	}

	@PostConstruct
	public void init() {
		presenter.setView(this);
	}

	private Grid<SportEvent> buildGrid() {
		final Grid<SportEvent> grid = new Grid<>(SportEvent.class);
		grid.setSizeFull();

		adjustGridCoumn(grid);
		grid.asSingleSelect()
				.addValueChangeListener(e -> showEventDetailForm(e.getValue()));

		return grid;
	}

	private void initFilters() {
		ListDataProvider<SportEvent> dataProvider = (ListDataProvider<SportEvent>) grid
				.getDataProvider();
		nameFilter.addValueChangeListener(e -> updateFilters(dataProvider));
	}

	private void initButtons() {
		createButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		createButton.setIcon(VaadinIcons.PLUS);
		createButton.addClickListener(clickEvent -> {
			eventDataForm.setSportEvent(MyBeanFactory.createNewSportEvent());
			grid.asSingleSelect().clear();
		});
	}

	private void adjustGridCoumn(final Grid<SportEvent> grid) {
		grid.addColumn(SportEvent::getName).setCaption("Event name");
		grid.addColumn(SportEvent::getLocation).setCaption("Location");
		grid.addColumn(SportEvent::getSportType).setCaption("Sport type");
		grid.setColumns("name", "location", "sportType");
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

	private void showEventDetailForm(SportEvent sportEvent) {
		if (sportEvent == null) {
			eventDataForm.setVisible(false);
		}
		else {
			eventDataForm.setVisible(true);
			eventDataForm.setSportEvent(sportEvent);
		}
	}

	public void setGridItems(List<SportEvent> sportEventByOrganizer) {
		grid.setItems(sportEventByOrganizer);
		initFilters();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		presenter.enter();
	}

	private class EventDataForm extends FormLayout {

		TextField nameField;
		TextField locationField;
		TextField maxParticipantField;
		TextField sportType;
		DateTimeField startDateTimeField;
		TextArea detailsArea;
		DateTimeField endDateTimeField;
		ComboBox<User> participantsComboBox;
		Button deleteParticipantButton;
		Button saveButton;
		Button deleteButton;
		Button locationButton;

		BeanValidationBinder<SportEvent> binder;

		public EventDataForm() {
			super();
			setMargin(false);
			addStyleName("event_form");
			setSizeUndefined();
			initComponent();
			initBinder();
			addComponents(nameField, sportType, maxParticipantField,
					startDateTimeField, endDateTimeField,
					new MHorizontalLayout(locationField, locationButton).withFullSize()
							.withAlign(locationButton, Alignment.BOTTOM_CENTER),
					new MHorizontalLayout(participantsComboBox,
							deleteParticipantButton).withFullSize()
							.withAlign(deleteParticipantButton, Alignment.BOTTOM_CENTER),
					detailsArea, new MHorizontalLayout(saveButton, deleteButton));
		}

		private void initComponent() {
			nameField = new TextField(NAME);
			locationField = new TextField(LOCATION);
			maxParticipantField = new TextField(MAX_PARTICIPANT);
			sportType = new TextField(SPORT_TYPE);
			startDateTimeField = new DateTimeField(START_DATE);
			endDateTimeField = new DateTimeField(END_DATE);
			detailsArea = new TextArea(DETAILS);
			participantsComboBox = new ComboBox<>(PARTICIPANTS);
			participantsComboBox.setTextInputAllowed(false);
			participantsComboBox.setEmptySelectionAllowed(false);
			participantsComboBox.setItemCaptionGenerator(
					user -> user.getUsername() + " / " + user.getRealName());
			deleteParticipantButton = new Button();
			deleteParticipantButton.setIcon(VaadinIcons.ERASER);
			deleteParticipantButton.addStyleName(ValoTheme.BUTTON_DANGER);
			deleteParticipantButton
					.addClickListener(clickEvent -> deleteParticipantFromEvent());
			saveButton = new Button(SAVE);
			saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			saveButton.addClickListener(clickEvent -> save());
			deleteButton = new Button(DELETE);
			deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
			deleteButton.addClickListener(clickEvent -> delete());
			locationButton = new Button();
			locationButton.setIcon(VaadinIcons.MAP_MARKER);
			locationButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			locationButton.addClickListener(clickEvent -> {
				adjustLocationPoint();
			});
		}

		private void initBinder() {
			binder = new BeanValidationBinder<>(SportEvent.class);
			binder.forField(nameField).asRequired(REQUIRED_MSG).bind(SportEvent::getName,
					SportEvent::setName);
			binder.forField(locationField).asRequired(REQUIRED_MSG)
					.bind(SportEvent::getLocation, SportEvent::setLocation);
			binder.forField(startDateTimeField).asRequired(REQUIRED_MSG)
					.bind(SportEvent::getStartDate, SportEvent::setStartDate);
			binder.forField(endDateTimeField).asRequired(REQUIRED_MSG)
					.bind(SportEvent::getEndDate, SportEvent::setEndDate);
			binder.forField(maxParticipantField)
					.withConverter(new StringToIntegerConverter(WRONG_INPUT))
					.bind(SportEvent::getMaxParticipant, SportEvent::setMaxParticipant);
			binder.forField(sportType).asRequired(REQUIRED_MSG)
					.bind(SportEvent::getSportType,
					SportEvent::setSportType);
			binder.forField(detailsArea).bind(SportEvent::getDetails,
					SportEvent::setDetails);
		}

		private void setSportEvent(SportEvent sportEvent) {
			binder.setBean(sportEvent);
			adjustComboBox(sportEvent);
			deleteButton.setVisible(!sportEvent.isNew());
			setVisible(true);
		}

		private void adjustComboBox(SportEvent sportEvent) {
			if (sportEvent.getParticipants().size() != 0) {
				participantsComboBox.setEnabled(true);
				deleteParticipantButton.setEnabled(true);
				participantsComboBox.setItems(sportEvent.getParticipants());
			}
			else {
				participantsComboBox.setEnabled(false);
				deleteParticipantButton.setEnabled(false);
				participantsComboBox.clear();
			}
		}

		private void save() {
			if (binder.isValid()) {
				presenter.save(binder.getBean());
				presenter.updateGridDate();
				setVisible(false);
			}
			else {
				showWarningNotification(VALIDATION_WARNING_MSG);
			}
		}

		private void delete() {
			if (binder.isValid()) {
				presenter.delete(binder.getBean());
				presenter.updateGridDate();
				setVisible(false);
			}
			else {
				showWarningNotification(VALIDATION_WARNING_MSG);
			}
		}

		private void adjustLocationPoint() {
			mapForm.constructMapForm(Optional.ofNullable(binder.getBean()), false);
			mapForm.showInWindow(getUI());
		}

		private void deleteParticipantFromEvent() {
			SportEvent sportEvent = binder.getBean();
			User user = participantsComboBox.getValue();
			if (sportEvent != null && user != null) {
				presenter.deleteParticipantFromEvent(sportEvent,
						user);
				participantsComboBox.clear();
				presenter.updateGridDate();
				grid.select(sportEvent);
			}
			else {
				showErrorNotification("Please choose a user before delete");
			}
		}
	}
}