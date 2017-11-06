package hu.szeged.sporteventapp.ui.views.eventviews;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;
import static hu.szeged.sporteventapp.ui.views.eventviews.ExploreEventView.VIEW_NAME;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javax.annotation.PostConstruct;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.teemu.switchui.Switch;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.data.enums.SportType;
import hu.szeged.sporteventapp.common.converter.LocalDateTimeConverter;
import hu.szeged.sporteventapp.common.util.DialogueUtil;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.custom_components.MapForm;
import hu.szeged.sporteventapp.ui.events.JumpToSelectedSportEvent;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "explore-events")
@SideBarItem(sectionId = Sections.EVENT, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.FILE_SEARCH)
@ViewScope
public class ExploreEventView extends AbstractView {

	public static final String VIEW_NAME = "Explore events";
	private static final String CENTER_ALIGN_STYLE = "v-align-center";
	private static final String PARTICIPANT_COLUMN_STYLE = "grid-participant-column";

	private final ExploreEventPresenter presenter;
	private final LocalDateTimeConverter timeConverter;
	private final MapForm mapForm;
	private final EventBus.UIEventBus eventBus;

	private Grid<SportEvent> grid;
	private TextField nameFilter;
	private TextField locationFilter;
	private ComboBox<String> sportTypeFilter;
	private DateTimeField fromDateField;
	private DateTimeField toDateField;
	private CheckBox freeSpaceCheckBox;
	private CheckBox participantCheckBox;
	// private Button joinButton;
	// private Button leaveButton;
	private Button detailsButton;
	private ParticipantWindow participantWindow;

	@Autowired
	public ExploreEventView(ExploreEventPresenter presenter, LocalDateTimeConverter timeConverter, MapForm mapForm,
			EventBus.UIEventBus eventBus) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.timeConverter = timeConverter;
		this.mapForm = mapForm;
		this.eventBus = eventBus;
	}

	@Override
	public void initComponent() {
		participantWindow = new ParticipantWindow();
		nameFilter = new TextField(NAME);
		locationFilter = new TextField(LOCATION);
		sportTypeFilter = new ComboBox<>(SPORT_TYPE);
		sportTypeFilter.setItems(SportType.getAllSportType());
		fromDateField = new DateTimeField(START_DATE_FROM);
		fromDateField.setWidth(185, Unit.PIXELS);
		toDateField = new DateTimeField(START_DATE_TO);
		toDateField.setWidth(185, Unit.PIXELS);
		freeSpaceCheckBox = new CheckBox(FREE_SPACE);
		participantCheckBox = new CheckBox(ALREADY_JOINED);
		// joinButton = new Button(JOIN);
		// leaveButton = new Button(LEAVE);
		detailsButton = new Button("Jump for " + DETAILS);
	}

	@Override
	public void initBody() {
		grid = buildGrid();
		initButton();
		addComponents(new MHorizontalLayout().withMargin(false).withFullWidth()
				.add(nameFilter, locationFilter, sportTypeFilter, fromDateField, toDateField)
				.withAlign(fromDateField, Alignment.MIDDLE_RIGHT).withAlign(toDateField, Alignment.MIDDLE_RIGHT),
				new MHorizontalLayout().withMargin(false).withFullWidth()
						.add(new MHorizontalLayout().add(freeSpaceCheckBox, participantCheckBox), Alignment.MIDDLE_LEFT)
						.add(new MHorizontalLayout().add(detailsButton/* , joinButton, leaveButton */),
								Alignment.MIDDLE_RIGHT));
		addComponentsAndExpand(grid);
	}

	@PostConstruct
	public void init() {
		presenter.setView(this);
	}

	private Grid<SportEvent> buildGrid() {
		final Grid<SportEvent> grid = new Grid<>(SportEvent.class);
		grid.setSizeFull();

		adjustGridCoumns(grid);

		return grid;
	}

	private void adjustGridCoumns(final Grid<SportEvent> grid) {
		grid.addColumn(SportEvent::getName).setCaption(NAME);
		grid.addColumn(SportEvent::getLocation).setCaption(LOCATION);
		grid.addColumn(SportEvent::getSportType).setCaption(SPORT_TYPE);
		grid.setColumns("name", "location", "sportType");
		grid.addColumn(
				sportEvent -> timeConverter.convertLocalDateTimeToString(sportEvent.getStartDate(), "yyyy.MM.dd HH:mm"))
				.setCaption(START).setWidth(170);
		grid.addColumn(
				sportEvent -> timeConverter.convertLocalDateTimeToString(sportEvent.getEndDate(), "yyyy.MM.dd HH:mm"))
				.setCaption(END).setWidth(170);
		grid.addColumn(sportEvent -> {
			long minutes = sportEvent.getStartDate().until(sportEvent.getEndDate(), ChronoUnit.MINUTES);

			return minutes / 60 + ":" + minutes % 60;
		}).setCaption("Duration(hour:min)");
		grid.addComponentColumn(this::generateParticipantColumnComponent).setStyleGenerator(e -> CENTER_ALIGN_STYLE)
				.setCaption(PARTICIPANTS);
		grid.addComponentColumn(sportEvent -> {
			Button button = new Button(VaadinIcons.GLOBE);
			button.addClickListener(c -> {
				mapForm.constructMapForm(Optional.ofNullable(sportEvent), true);
				DialogueUtil.showInWindow(getUI(), mapForm, mapForm.CAPTION, VaadinIcons.MAP_MARKER, 800, 600);
			});
			return button;
		}).setStyleGenerator(e -> CENTER_ALIGN_STYLE).setCaption(LOCATION);
		grid.addComponentColumn(sportEvent -> {
			Switch button = new Switch();
			button.setValue(presenter.isParticipant(sportEvent));
			button.addValueChangeListener(vc -> {
				if (button.getValue()) {
					join(sportEvent);
				} else {
					leave(sportEvent);
				}
			});
			return button;
		}).setStyleGenerator(e -> CENTER_ALIGN_STYLE).setCaption("????");
	}

	private CssLayout generateParticipantColumnComponent(SportEvent sportEvent) {
		CssLayout layout = new CssLayout();
		layout.addStyleName(PARTICIPANT_COLUMN_STYLE);
		Button button = new Button(VaadinIcons.GROUP);
		button.addClickListener(c -> {
			participantWindow.setUsers(sportEvent.getParticipants());
			getUI().addWindow(participantWindow);
		});
		String labelText = String.format("%3d/%3d", sportEvent.getParticipants().size(),
				sportEvent.getMaxParticipant());
		layout.addComponents(new Label(labelText, ContentMode.PREFORMATTED), button);
		return layout;
	}

	private void initFilters() {
		ListDataProvider<SportEvent> dataProvider = (ListDataProvider<SportEvent>) grid.getDataProvider();
		bindFiltersAndGrid(dataProvider, sportTypeFilter, nameFilter, locationFilter, fromDateField, toDateField,
				freeSpaceCheckBox, participantCheckBox);
	}

	private void initButton() {
		detailsButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		detailsButton.setIcon(VaadinIcons.EYE);
		detailsButton.addClickListener(clickEvent -> jumpToSelectEvent());
		// joinButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		// joinButton.setIcon(VaadinIcons.FLAG_CHECKERED);
		// joinButton.addClickListener(clickEvent -> {
		// MessageBox.createQuestion().asModal(true).withCaption("Question Dialog")
		// .withMessage("Do you really want to join?").withYesButton(() -> join()).withNoButton().open();
		// });
		// leaveButton.setStyleName(ValoTheme.BUTTON_DANGER);
		// leaveButton.setIcon(VaadinIcons.EXIT_O);
		// leaveButton.addClickListener(clickEvent -> leave());
	}

	private void updateFilters(final ListDataProvider<SportEvent> dataProvider) {
		dataProvider.clearFilters();
		dataProvider.addFilter(sportEvent -> caseInsensitiveContains(sportEvent.getName(), nameFilter.getValue()));
		dataProvider
				.addFilter(sportEvent -> caseInsensitiveContains(sportEvent.getLocation(), locationFilter.getValue()));
		dataProvider.addFilter(sportEvent -> caseInsensitiveContains(sportEvent.getSportType(),
				Strings.nullToEmpty(sportTypeFilter.getValue())));
		if (freeSpaceCheckBox.getValue()) {
			dataProvider.addFilter(sportEvent -> sportEvent.getParticipants().size() < sportEvent.getMaxParticipant());
		}
		if (participantCheckBox.getValue()) {
			dataProvider.addFilter(sportEvent -> sportEvent.getParticipants().contains(presenter.getSessionUser()));
		}
		if (fromDateField.getOptionalValue().isPresent() && toDateField.getOptionalValue().isPresent()) {
			dataProvider.addFilter(sportEvent -> filterByDateRange(sportEvent.getStartDate(), fromDateField.getValue(),
					toDateField.getValue()));
		}
	}

	private void bindFiltersAndGrid(final ListDataProvider<SportEvent> dataProvider, ComboBox<String> comboBox,
			AbstractField... fields) {
		comboBox.addValueChangeListener(e -> updateFilters(dataProvider));
		Arrays.stream(fields).forEach(field -> field.addValueChangeListener(e -> updateFilters(dataProvider)));
	}

	private Boolean caseInsensitiveContains(String where, String what) {
		return where.toLowerCase().contains(what.toLowerCase());
	}

	private Boolean filterByDateRange(LocalDateTime time, LocalDateTime from, LocalDateTime to) {
		return time.isAfter(from) && time.isBefore(to);
	}

	public void jumpToSelectEvent() {
		SportEvent sportEvent = grid.asSingleSelect().getValue();
		if (sportEvent != null) {
			getUI().getNavigator().navigateTo("event");
			eventBus.publish(this, new JumpToSelectedSportEvent(this, sportEvent));
		} else {
			showInfoNotification(PLEASE_SELECT_A_ROW);
		}
	}

	public void join(SportEvent sportEvent) {
		if (sportEvent != null) {
			presenter.join(sportEvent);
		} else {
			showInfoNotification(PLEASE_SELECT_A_ROW);
		}
	}

	public void leave(SportEvent sportEvent) {
		if (sportEvent != null) {
			presenter.leave(sportEvent);
		} else {
			showInfoNotification(PLEASE_SELECT_A_ROW);
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

	private class ParticipantWindow extends Window {

		Grid<User> grid;

		public ParticipantWindow() {
			super();
			initContent();
		}

		private void initContent() {
			setWidth(500, Unit.PIXELS);
			setHeight(400, Unit.PIXELS);
			setIcon(VaadinIcons.GROUP);
			setCaption(PARTICIPANTS);
			setResizable(false);
			setDraggable(false);
			center();
			grid = buildGrid();
			setContent(grid);
		}

		private Grid<User> buildGrid() {
			final Grid<User> grid = new Grid<>(User.class);
			grid.setSizeFull();
			grid.setColumns("username", "realName");
			grid.setSelectionMode(Grid.SelectionMode.NONE);
			return grid;
		}

		public void setUsers(Set<User> users) {
			if (users == null) {
				grid.setItems(new HashSet<>());
			} else {
				grid.setItems(users);
			}
		}
	}
}
