package hu.szeged.sporteventapp.ui.views.eventviews;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;
import static hu.szeged.sporteventapp.ui.views.eventviews.SingleEventView.VIEW_NAME;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.converter.LocalDateTimeConverter;
import hu.szeged.sporteventapp.common.factory.MyBeanFactory;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.custom_components.MapForm;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "event")
@SideBarItem(sectionId = Sections.EVENT, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.STAR_O)
public class SingleEventView extends AbstractView {

	public static final String VIEW_NAME = "Event";

	private final SingleEventPresenter presenter;
	private final LocalDateTimeConverter timeConverter;

	private MVerticalLayout readOnlyDataForm;
	private MapForm mapForm;
	private Grid<User> userGrid;
	private Button participantsButton;
	private Button locationButton;
	private Button mediaBoardButton;

	@Autowired
	public SingleEventView(SingleEventPresenter presenter,
			LocalDateTimeConverter timeConverter, MapForm mapForm) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.timeConverter = timeConverter;
		this.mapForm = mapForm;
		initReadOnlyDataForm(MyBeanFactory.createNewSportEvent());
	}

	@Override
	public void initComponent() {
		userGrid = new Grid<>(User.class);
		userGrid.setSizeFull();
		readOnlyDataForm = new MVerticalLayout();
		readOnlyDataForm.addStyleName("data-layout");
		participantsButton = new Button(PARTICIPANTS);
		locationButton = new Button(LOCATION);
		mediaBoardButton = new Button(MEDIA);
	}

	@Override
	public void initBody() {
		initGrid();
		initCommandButtons();
		addComponentsAndExpand(new MHorizontalLayout().withMargin(false)
				.add(new MVerticalLayout().withStyleName("event-data-viewer").add(
						readOnlyDataForm, participantsButton, locationButton,
						mediaBoardButton)));
	}

	@PostConstruct
	public void init() {
		presenter.setView(this);
	}

	private void initGrid() {
		userGrid.setColumns("username", "realName", "age", "email");
	}

	private void initCommandButtons() {
		participantsButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		participantsButton.setIcon(VaadinIcons.GROUP);
		locationButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		locationButton.setIcon(VaadinIcons.MAP_MARKER);
		mediaBoardButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		mediaBoardButton.setIcon(VaadinIcons.MOVIE);
	}

	private void initReadOnlyDataForm(SportEvent sportEvent) {
		readOnlyDataForm.with(
				new MLabel(ORGANIZER, sportEvent.getOrganizer().getUsername()),
				new MLabel(LOCATION, sportEvent.getLocation()),
				new MLabel(SPORT_TYPE, sportEvent.getSportType()),
				new MLabel(DATE_FROM,
						timeConverter
								.convertLocalDateTimeToString(sportEvent.getStartDate())),
				new MLabel(DATE_TO,
						timeConverter
								.convertLocalDateTimeToString(sportEvent.getEndDate())),
				new MLabel(MAX_PARTICIPANT,
						String.valueOf(sportEvent.getMaxParticipant())),
				new MLabel(DETAILS, sportEvent.getDetails()));
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		presenter.enter();
	}
}
