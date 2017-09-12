package hu.szeged.sporteventapp.ui.views.calenarview;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.SportEventService;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;
import hu.szeged.sporteventapp.ui.views.IPresenter;

@UIScope
@SpringComponent
public class CalendarPresenter extends AbstractPresenter<CalendarView>
		implements IPresenter {

	private SportEventService sportEventService;
	private UserService userService;
	private User sessionUser;

	public CalendarPresenter(SportEventService sportEventService,
			UserService userService) {
		this.sportEventService = sportEventService;
		this.userService = userService;
	}

	private void updateCalendarData() {
		getView().setCalendarItems(
				sportEventService.findSportEventByOrganizer(sessionUser));
	}

	@Override
	public void enter() {
		sessionUser = userService.getCurrentUser();
		updateCalendarData();
	}
}
