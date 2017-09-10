package hu.szeged.sporteventapp.ui.views.myeventview;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.SportEventService;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;
import hu.szeged.sporteventapp.ui.views.IPresenter;

@UIScope
@SpringComponent
public class MyEventPresenter extends AbstractPresenter<MyEventView>
		implements IPresenter, Serializable {

	private SportEventService sportEventService;
	private UserService userService;
	private User sessionUser;

	@Autowired
	public MyEventPresenter(SportEventService sportEventService,
			UserService userService) {
		this.sportEventService = sportEventService;
		this.userService = userService;
	}

	public void save(SportEvent sportEvent) {
		sportEvent.setOrganizer(sessionUser);
		sportEventService.save(sportEvent);
	}

	public void delete(SportEvent sportEvent) {
		sportEventService.delete(sportEvent);
	}

	public void deleteParticipantFromEvent(SportEvent event, User user) {
		sportEventService.deleteParticipantFromEvent(event, user);
	}

	public void updateGridDate() {
		getView().setGridItems(sportEventService.findSportEventByOrganizer(sessionUser));
	}

	public void enter() {
		sessionUser = userService.getCurrentUser();
		updateGridDate();
	}
}
