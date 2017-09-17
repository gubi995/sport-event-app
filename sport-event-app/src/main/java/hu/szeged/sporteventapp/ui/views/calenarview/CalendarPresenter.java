package hu.szeged.sporteventapp.ui.views.calenarview;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;
import hu.szeged.sporteventapp.ui.views.IPresenter;

@UIScope
@SpringComponent
public class CalendarPresenter extends AbstractPresenter<CalendarView>
		implements IPresenter {

	private UserService userService;
	private User sessionUser;

	public CalendarPresenter(UserService userService) {
		this.userService = userService;
	}

	private void updateCalendarData() {
		getView().setCalendarItems(sessionUser.getEventsImAttending());
	}

	@Override
	public void enter() {
		sessionUser = userService.getCurrentUser();
		updateCalendarData();
	}
}
