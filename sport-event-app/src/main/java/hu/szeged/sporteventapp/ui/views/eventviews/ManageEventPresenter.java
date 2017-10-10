package hu.szeged.sporteventapp.ui.views.eventviews;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.SportEventService;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;

@UIScope
@SpringComponent
public class ManageEventPresenter extends AbstractPresenter<ManageEventView> {

	private final SportEventService sportEventService;

	@Autowired
	public ManageEventPresenter(SportEventService sportEventService, UserService userService) {
		super(userService);
		this.sportEventService = sportEventService;
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

	@Override
	public void enter() {
		super.enter();
		updateGridDate();
	}
}
