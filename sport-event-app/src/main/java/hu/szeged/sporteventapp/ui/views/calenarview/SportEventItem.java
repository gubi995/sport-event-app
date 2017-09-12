package hu.szeged.sporteventapp.ui.views.calenarview;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.vaadin.addon.calendar.client.ui.schedule.CalendarItem;
import org.vaadin.addon.calendar.item.BasicItem;

import com.vaadin.icons.VaadinIcons;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;

public class SportEventItem extends BasicItem {

	private final SportEvent sportEvent;

	public SportEventItem(SportEvent sportEvent) {
		super(sportEvent.getName(), sportEvent.getDetails(),
				ZonedDateTime.of(sportEvent.getStartDate(), ZoneId.systemDefault()),
				ZonedDateTime.of(sportEvent.getEndDate(), ZoneId.systemDefault()));
		this.sportEvent = sportEvent;
	}

	public SportEvent getSportEvent() {
		return sportEvent;
	}

	@Override
	public boolean isMoveable() {
		return false;
	}

	@Override
	public boolean isResizeable() {
		return false;
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public String getDateCaptionFormat() {
		return VaadinIcons.CLOCK.getHtml() + " " + CalendarItem.RANGE_TIME;
	}

	@Override
	public String getCaption() {
		return "Event name: " + super.getCaption();
	}

	@Override
	public String getDescription() {
		return "Description: " + super.getDescription();
	}
}
