package hu.szeged.sporteventapp.ui.views.eventview;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;
import static hu.szeged.sporteventapp.ui.views.eventview.ExploreEventView.VIEW_NAME;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.themes.ValoTheme;
import hu.szeged.sporteventapp.backend.data.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.converter.LocalDateTimeConverter;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "explore-events")
@SideBarItem(sectionId = Sections.EVENT, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.FILE_SEARCH)
public class ExploreEventView extends AbstractView implements View, Serializable {

	public static final String VIEW_NAME = "Explore events";

	private final ExploreEventPresenter presenter;
	private final LocalDateTimeConverter timeConverter;

	private Grid<SportEvent> grid;
	private TextField nameFilter;
	private TextField locationFilter;
	private DateTimeField fromDateField;
	private DateTimeField toDateField;
	private CheckBox freeSpaceComboBox;
	private Button joinButton;
	private Button leaveButton;
	private ParticipantWindow participantWindow;

	@Autowired
	public ExploreEventView(ExploreEventPresenter presenter,
			LocalDateTimeConverter timeConverter) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.timeConverter = timeConverter;
		addStyleName("explore_event");
	}

	@Override
	public void initBody() {
		grid = buildGrid();
		initFilters();
		initButton();
		addComponent(new MHorizontalLayout()
				.add(nameFilter, locationFilter, fromDateField, toDateField,
						freeSpaceComboBox, joinButton, leaveButton)
				.alignAll(Alignment.BOTTOM_CENTER));
		addComponentsAndExpand(grid);
		participantWindow = new ParticipantWindow();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

	}

	private Grid<SportEvent> buildGrid() {
		final Grid<SportEvent> grid = new Grid<>(SportEvent.class);
		grid.setSizeFull();

		grid.setItems(generateDataForTest());
		adjustGridCoumns(grid);

		return grid;
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
		grid.addColumn(sportEvent -> "Participants", new ButtonRenderer<>(e -> {
			participantWindow.setUsers(e.getItem().getParticipants());
			getUI().addWindow(participantWindow);
		})).setCaption("Participants");
		grid.addColumn(sportEvent -> "Location", new ButtonRenderer<>(e -> {
			Notification.show("Show location");
		})).setCaption("Location");
	}

	private void initFilters() {
		ListDataProvider<SportEvent> dataProvider = (ListDataProvider<SportEvent>) grid
				.getDataProvider();
		nameFilter = new TextField(NAME);
		locationFilter = new TextField(LOCATION);
		fromDateField = new DateTimeField(START_DATE_FROM);
		toDateField = new DateTimeField(START_DATE_TO);
		freeSpaceComboBox = new CheckBox(IS_THERE_FREE_SPACE);
		bindFiltersAndGrid(dataProvider, nameFilter, locationFilter, fromDateField,
				toDateField, freeSpaceComboBox);
	}

	private void initButton(){
		joinButton = new Button(JOIN);
		joinButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		joinButton.setIcon(VaadinIcons.FLAG_CHECKERED);
		leaveButton = new Button(LEAVE);
		leaveButton.setStyleName(ValoTheme.BUTTON_DANGER);
		leaveButton.setIcon(VaadinIcons.EXIT_O);
	}

	private void updateFilters(final ListDataProvider<SportEvent> dataProvider) {
		dataProvider.clearFilters();
		dataProvider.addFilter(sportEvent -> caseInsensitiveContains(sportEvent.getName(),
				nameFilter.getValue()));
		dataProvider
				.addFilter(sportEvent -> caseInsensitiveContains(sportEvent.getLocation(),
						locationFilter.getValue()));
		if (fromDateField.getOptionalValue().isPresent()
				&& toDateField.getOptionalValue().isPresent()) {
			dataProvider
					.addFilter(sportEvent -> filterByDateRange(sportEvent.getStartDate(),
							fromDateField.getValue(), toDateField.getValue()));
		}
	}

	private void bindFiltersAndGrid(final ListDataProvider<SportEvent> dataProvider,
			AbstractField... fields) {
		Arrays.stream(fields).forEach(
				field -> field.addValueChangeListener(e -> updateFilters(dataProvider)));
	}

	private Boolean caseInsensitiveContains(String where, String what) {
		return where.toLowerCase().contains(what.toLowerCase());
	}

	private Boolean filterByDateRange(LocalDateTime time, LocalDateTime from,
			LocalDateTime to) {
		return time.isAfter(from) && time.isBefore(to);
	}

	public static List<SportEvent> generateDataForTest() {
		ArrayList<SportEvent> tests = new ArrayList();
		tests.add(new SportEvent("1", "a", LocalDateTime.now(),
				LocalDateTime.of(2017, 9, 7, 23, 50, 50), 20, "a1", new User(), null));
		tests.add(new SportEvent("12", "d", LocalDateTime.now(),
				LocalDateTime.of(2017, 9, 12, 13, 50, 50), 20, "a1", new User(), null));
		tests.add(new SportEvent("13", "adff", LocalDateTime.now(), LocalDateTime.now(),
				20, "a1", new User(), null));
		tests.add(new SportEvent("14", "a", LocalDateTime.of(2017, 9, 12, 13, 50, 50),
				LocalDateTime.now(), 20, "a1", new User(), null));
		tests.add(new SportEvent("15", "gsdfa", LocalDateTime.of(2017, 9, 12, 13, 50, 50),
				LocalDateTime.now(), 20, "a1", new User(), null));
		tests.add(new SportEvent("16", "a", LocalDateTime.of(2017, 11, 22, 13, 50, 50),
				LocalDateTime.now(), 20, "a1", new User(), null));
		tests.add(new SportEvent("17", "a", LocalDateTime.now(), LocalDateTime.now(), 20,
				"a1", new User(), null));
		tests.add(new SportEvent("18", "aasd", LocalDateTime.now(), LocalDateTime.now(),
				20, "a1", new User(), null));
		tests.add(new SportEvent("18", "adds", LocalDateTime.now(), LocalDateTime.now(),
				20, "a1", new User(), null));
		tests.add(new SportEvent("18", "a", LocalDateTime.now(), LocalDateTime.now(), 20,
				"a1", new User(), null));
		tests.add(new SportEvent("18", "gh", LocalDateTime.now(), LocalDateTime.now(), 20,
				"a1", new User(), null));
		tests.add(new SportEvent("18", "aghgt", LocalDateTime.now(), LocalDateTime.now(),
				20, "a1", new User(), null));
		tests.add(new SportEvent("18", "a", LocalDateTime.now(), LocalDateTime.now(), 20,
				"a1", new User(), null));
		List<User> users = new ArrayList<>();
		users.add(new User("adi", "a@a.com","a","a", Role.USER,5,"a","d",null));
		users.add(new User("adi2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("adi2sdf", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("adisdf2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("asddi2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("adfi2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("adsdfsadfi2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("adasdfi2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("adasdfasdfi2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("adasdfsadi2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("adasdfsdfi2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		users.add(new User("adasdfasdfi2", "a@a.com","afd","ss", Role.USER,55,"a","dd",null));
		tests.add(new SportEvent("88", "a", LocalDateTime.now(), LocalDateTime.now(), 20,
				"a1", new User(), users));
		return tests;
	}

	private class ParticipantWindow extends Window{

		Grid<User> grid;

		public ParticipantWindow() {
			super();
			initContent();
		}

		private void initContent(){
			setWidth(500,Unit.PIXELS);
			setHeight(400, Unit.PIXELS);
			setIcon(VaadinIcons.GROUP);
			setCaption(PARTICIPANTS);
			setResizable(false);
			setDraggable(false);
			center();
			grid = buildGrid();
			setContent(grid);
		}

		private Grid<User> buildGrid(){
			final Grid<User> grid = new Grid<>(User.class);
			grid.setSizeFull();
			grid.setColumns("username","realName");
			grid.setSelectionMode(Grid.SelectionMode.NONE);
			return grid;
		}

		public void setUsers(List<User> users){
			if(users == null) {
				grid.setItems(new ArrayList<>());
			}else {
				grid.setItems(users);
			}
		}
	}
}
