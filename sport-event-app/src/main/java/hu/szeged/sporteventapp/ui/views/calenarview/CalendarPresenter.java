package hu.szeged.sporteventapp.ui.views.calenarview;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;

@UIScope
@SpringComponent
public class CalendarPresenter extends AbstractPresenter<CalendarView> {

	public CalendarPresenter(UserService userService) {
		super(userService);
	}

	private void updateCalendarData() {
		getView().setCalendarItems(sessionUser.getEventsImAttending());
	}

	@Override
	public void enter() {
		super.enter();
		updateCalendarData();
	}
}
