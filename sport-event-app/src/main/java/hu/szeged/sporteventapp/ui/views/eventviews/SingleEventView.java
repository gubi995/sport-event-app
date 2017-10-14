package hu.szeged.sporteventapp.ui.views.eventviews;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.converter.LocalDateTimeConverter;
import hu.szeged.sporteventapp.ui.custom_components.MapForm;
import hu.szeged.sporteventapp.ui.custom_components.MessageBoardForm;
import hu.szeged.sporteventapp.ui.custom_components.ParticipantForm;
import hu.szeged.sporteventapp.ui.events.JumpToSelectedSportEvent;
import hu.szeged.sporteventapp.ui.listeners.JumpToSelectedEventListener;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "event")
@VaadinFontIcon(VaadinIcons.STAR_O)
public class SingleEventView extends AbstractView implements JumpToSelectedEventListener {

	public static final String VIEW_NAME = "Event";
	private static final String DATA_LAYOUT = "data-layout";
	private static final String EVENT_DATA_VIEWER = "event-data-viewer";
	private static final String NORMAL_SPACE = "normal-white-space";

	private final SingleEventPresenter presenter;
	private final EventBus.UIEventBus eventBus;
	private final LocalDateTimeConverter timeConverter;
	private final MapForm mapForm;
	private final ParticipantForm participantForm;
	private final MessageBoardForm messageBoardForm;

	private MVerticalLayout readOnlyDataForm;
	private MHorizontalLayout contentLayout;
	private Grid<User> userGrid;
	private Button participantsButton;
	private Button locationButton;
	private Button mediaBoardButton;

	private Binder<SportEvent> binder;

	@Autowired
	public SingleEventView(SingleEventPresenter presenter, EventBus.UIEventBus eventBus,
			LocalDateTimeConverter timeConverter, MapForm mapForm, ParticipantForm participantForm,
			MessageBoardForm messageBoardForm) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.eventBus = eventBus;
		this.timeConverter = timeConverter;
		this.mapForm = mapForm;
		this.participantForm = participantForm;
		this.messageBoardForm = messageBoardForm;
		eventBus.subscribe(this);
	}

	@Override
	public void initComponent() {
		userGrid = new Grid<>(User.class);
		userGrid.setSizeFull();
		readOnlyDataForm = new MVerticalLayout();
		readOnlyDataForm.addStyleName(DATA_LAYOUT);
		participantsButton = new Button(PARTICIPANTS);
		locationButton = new Button(LOCATION);
		mediaBoardButton = new Button(MEDIA);
		contentLayout = new MHorizontalLayout();

		binder = new Binder<>(SportEvent.class);
	}

	@Override
	public void initBody() {
		initGrid();
		initCommandButtons();
		addComponentsAndExpand(contentLayout.withMargin(false).withFullSize()
				.add(new MVerticalLayout().withStyleName(EVENT_DATA_VIEWER).add(readOnlyDataForm, participantsButton,
						locationButton, mediaBoardButton)));
	}

	@PostConstruct
	public void init() {
		presenter.setView(this);
		contentLayout.add(messageBoardForm);
	}

	private void initGrid() {
		userGrid.setColumns("username", "realName", "age", "email");
	}

	private void initCommandButtons() {
		participantsButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		participantsButton.setIcon(VaadinIcons.GROUP);
		participantsButton.addClickListener(clickEvent -> {
			participantForm.constructParticipantForm(Optional.ofNullable(binder.getBean().getParticipants()));
			participantForm.showInWindow(getUI());
		});
		locationButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		locationButton.setIcon(VaadinIcons.MAP_MARKER);
		locationButton.addClickListener(clickEvent -> {
			mapForm.constructMapForm(Optional.ofNullable(binder.getBean()), true);
			mapForm.showInWindow(getUI());
		});
		mediaBoardButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		mediaBoardButton.setIcon(VaadinIcons.MOVIE);
	}

	private void initReadOnlyDataForm(SportEvent sportEvent) {
		readOnlyDataForm.with(new MLabel(ORGANIZER, sportEvent.getOrganizer().getUsername()),
				new MLabel(LOCATION, sportEvent.getLocation()), new MLabel(SPORT_TYPE, sportEvent.getSportType()),
				new MLabel(DATE_FROM, timeConverter.convertLocalDateTimeToString(sportEvent.getStartDate())),
				new MLabel(DATE_TO, timeConverter.convertLocalDateTimeToString(sportEvent.getEndDate())),
				new MLabel(MAX_PARTICIPANT, String.valueOf(sportEvent.getMaxParticipant())),
				new MLabel(DETAILS, sportEvent.getDetails()).withStyleName(NORMAL_SPACE));
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		presenter.enter();
	}

	@EventBusListenerMethod
	public void onJump(JumpToSelectedSportEvent event) {
		SportEvent sportEvent = event.getSportEvent();
		binder.setBean(sportEvent);
		setCaptionLabelText(sportEvent.getName());
		initReadOnlyDataForm(sportEvent);
		messageBoardForm.setMessageBoard(sportEvent.getMessageBoard());
		eventBus.unsubscribe(this);
	}
}
