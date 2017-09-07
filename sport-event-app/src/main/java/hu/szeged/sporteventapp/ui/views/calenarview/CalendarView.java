package hu.szeged.sporteventapp.ui.views.calenarview;

import static hu.szeged.sporteventapp.ui.views.calenarview.CalendarView.VIEW_NAME;

import java.io.Serializable;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;

import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "calendar")
@SideBarItem(sectionId = Sections.EVENT, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.CALENDAR)
public class CalendarView extends AbstractView implements View, Serializable {

	public static final String VIEW_NAME = "Calendar";

	private final CalendarPresenter presenter;

	private Calendar calendar;

	@Autowired
	public CalendarView(CalendarPresenter presenter) {
		super(VIEW_NAME);
		this.presenter = presenter;
	}

	@Override
	public void initBody() {
		calendar = new Calendar();
		calendar.setSizeFull();
		addComponentsAndExpand(calendar);
	}
}
