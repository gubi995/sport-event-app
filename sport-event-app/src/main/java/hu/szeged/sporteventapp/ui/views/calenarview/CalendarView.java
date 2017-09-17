package hu.szeged.sporteventapp.ui.views.calenarview;

import static hu.szeged.sporteventapp.ui.views.calenarview.CalendarView.VIEW_NAME;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.item.BasicItemProvider;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.common.converter.LocalDateTimeConverter;
import hu.szeged.sporteventapp.common.converter.SportEventConverter;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "calendar")
@SideBarItem(sectionId = Sections.EVENT, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.CALENDAR)
public class CalendarView extends AbstractView {

	public static final String VIEW_NAME = "Calendar";

	private final CalendarPresenter presenter;
	private final SportEventConverter eventConverter;
	private final LocalDateTimeConverter converter;

	private SportEventItemDataProvider dataProvider;
	private Calendar<SportEventItem> calendar;

	@Autowired
	public CalendarView(CalendarPresenter presenter, SportEventConverter eventConverter,
			LocalDateTimeConverter converter) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.eventConverter = eventConverter;
		this.converter = converter;
	}

	@Override
	public void initComponent() {
		dataProvider = new SportEventItemDataProvider();
		calendar = new Calendar();
		calendar.setCaptionAsHtml(true);
		calendar.setItemCaptionAsHtml(true);
		calendar.setResponsive(true);
	}

	@Override
	public void initBody() {
		calendar.setSizeFull();
		addComponentsAndExpand(calendar);
	}

	@PostConstruct
	public void init() {
		presenter.setView(this);
	}

	public void setCalendarItems(List<SportEvent> sportEventByOrganizer) {
		dataProvider.setItems(eventConverter
				.convertSportEventsToSportEventItems(sportEventByOrganizer));
		calendar.setDataProvider(dataProvider);
		calendar.setHandler(this::onCalendarClick);
	}

	private void onCalendarClick(CalendarComponentEvents.ItemClickEvent event) {
		SportEventItem item = (SportEventItem) event.getCalendarItem();
		final SportEvent sportEvent = item.getSportEvent();
		showCalendarData(sportEvent);
	}

	private void showCalendarData(SportEvent sportEvent) {
		Notification notification = new Notification("Event information",
				"Name: " + sportEvent.getName() + "<br>" + "Location: "
						+ sportEvent.getLocation() + "<br>" + "Start: "
						+ converter.convertLocalDateTimeToString(
								sportEvent.getStartDate())
						+ "<br>" + "End: "
						+ converter.convertLocalDateTimeToString(sportEvent.getEndDate())
						+ "<br>" + "Sport type: " + sportEvent.getSportType() + "<br>",
				Notification.Type.TRAY_NOTIFICATION);
		notification.setHtmlContentAllowed(true);
		notification.setIcon(VaadinIcons.VAADIN_V);
		notification.show(Page.getCurrent());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		presenter.enter();
	}

	private class SportEventItemDataProvider extends BasicItemProvider<SportEventItem> {

	}
}
