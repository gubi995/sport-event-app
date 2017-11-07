package hu.szeged.sporteventapp.ui.views.eventviews;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;
import static hu.szeged.sporteventapp.ui.custom_components.WrappedUpload.*;

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
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Upload;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.converter.LocalDateTimeConverter;
import hu.szeged.sporteventapp.common.util.DialogueUtil;
import hu.szeged.sporteventapp.common.util.ResourceUtil;
import hu.szeged.sporteventapp.ui.custom_components.*;
import hu.szeged.sporteventapp.ui.events.JumpToSelectedSportEvent;
import hu.szeged.sporteventapp.ui.listeners.JumpToSelectedEventListener;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "event")
@VaadinFontIcon(VaadinIcons.STAR_O)
@ViewScope
public class SingleEventView extends AbstractView implements JumpToSelectedEventListener, Upload.SucceededListener {

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
	private final MediaViewer mediaViewer;
	private final WrappedUpload upload;

	private MVerticalLayout readOnlyDataForm;
	private MVerticalLayout leftSideLayout;
	private MHorizontalLayout contentLayout;
	private Grid<User> userGrid;
	private Button participantsButton;
	private Button locationButton;
	private Button mediaButton;

	private Binder<SportEvent> binder;

	@Autowired
	public SingleEventView(SingleEventPresenter presenter, EventBus.UIEventBus eventBus,
			LocalDateTimeConverter timeConverter, MapForm mapForm, ParticipantForm participantForm,
			MessageBoardForm messageBoardForm, MediaViewer mediaViewer, WrappedUpload upload) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.eventBus = eventBus;
		this.timeConverter = timeConverter;
		this.mapForm = mapForm;
		this.participantForm = participantForm;
		this.messageBoardForm = messageBoardForm;
		this.mediaViewer = mediaViewer;
		this.upload = upload;
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
		mediaButton = new Button(MEDIA);
		contentLayout = new MHorizontalLayout();

		binder = new Binder<>(SportEvent.class);
	}

	@Override
	public void initBody() {
		initGrid();
		initCommandButtons();
		leftSideLayout = createLeftSide();
		addComponentsAndExpand(contentLayout.withMargin(false).withFullSize().add(leftSideLayout));

	}

	private MVerticalLayout createLeftSide() {
		MVerticalLayout layout = new MVerticalLayout();
		layout.withStyleName(EVENT_DATA_VIEWER).add(readOnlyDataForm, participantsButton, locationButton, mediaButton);
		return layout;
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
			DialogueUtil.showInWindow(getUI(), participantForm, PARTICIPANTS, VaadinIcons.GROUP);
		});
		locationButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		locationButton.setIcon(VaadinIcons.MAP_MARKER);
		locationButton.addClickListener(clickEvent -> {
			mapForm.constructMapForm(Optional.ofNullable(binder.getBean()), true);
			DialogueUtil.showInWindow(getUI(), mapForm, mapForm.CAPTION, VaadinIcons.MAP_MARKER, 800, 600);
		});
		mediaButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		mediaButton.setIcon(VaadinIcons.CARET_SQUARE_RIGHT_O);
		mediaButton.addClickListener(clickEvent -> {
			mediaViewer.setMedia(binder.getBean().getAlbum());
			DialogueUtil.showInWindow(getUI(), mediaViewer, mediaViewer.CAPTION, VaadinIcons.CARET_SQUARE_RIGHT_O);
		});
	}

	private void initReadOnlyDataForm(SportEvent sportEvent) {
		readOnlyDataForm.with(new MLabel(ORGANIZER, sportEvent.getOrganizer().getUsername()),
				new MLabel(LOCATION, sportEvent.getLocation()), new MLabel(SPORT_TYPE, sportEvent.getSportType()),
				new MLabel(DATE_FROM, timeConverter.convertLocalDateTimeToString(sportEvent.getStartDate())),
				new MLabel(DATE_TO, timeConverter.convertLocalDateTimeToString(sportEvent.getEndDate())),
				new MLabel(MAX_PARTICIPANT, String.valueOf(sportEvent.getMaxParticipant())),
				new MLabel(DETAILS, sportEvent.getDetails()).withStyleName(NORMAL_SPACE));
	}

	private void initWrappedUploader(SportEvent sportEvent) {
		boolean isAbleToUpdate = presenter.currentUserIsOrganizer(sportEvent.getOrganizer());
		if (isAbleToUpdate) {
			upload.setContext(ResourceUtil.EVENT);
			upload.setAllowedMimeType(JPG, PNG, OGG, MP4);
			upload.getUpload().addSucceededListener(this);
			leftSideLayout.add(upload);
		}
		upload.setVisible(isAbleToUpdate);
	}

	@Override
	public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
		presenter.saveMedia(succeededEvent);
		mediaViewer.setMedia(binder.getBean().getAlbum());
	}

	public Binder<SportEvent> getBinder() {
		return binder;
	}

	@EventBusListenerMethod
	public void onJump(JumpToSelectedSportEvent event) {
		SportEvent sportEvent = event.getSportEvent();
		binder.setBean(sportEvent);
		presenter.setSportEvent(sportEvent);
		setCaptionLabelText(sportEvent.getName());
		initReadOnlyDataForm(sportEvent);
		initWrappedUploader(sportEvent);
		messageBoardForm.setSportEvent(sportEvent);
		eventBus.unsubscribe(this);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		presenter.enter();
	}

	@Override
	public void detach() {
		upload.getUpload().removeSucceededListener(this);
		super.detach();
	}
}
