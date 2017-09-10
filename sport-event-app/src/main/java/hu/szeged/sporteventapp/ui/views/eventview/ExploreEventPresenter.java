package hu.szeged.sporteventapp.ui.views.eventview;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.SportEventService;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.common.exception.AlreadyJoinedException;
import hu.szeged.sporteventapp.common.exception.NoEmptyPlaceException;
import hu.szeged.sporteventapp.common.exception.NotParticipantException;
import hu.szeged.sporteventapp.ui.AbstractPresenter;

@UIScope
@SpringComponent
public class ExploreEventPresenter extends AbstractPresenter<ExploreEventView> {

	private SportEventService sportEventService;
	private UserService userService;
	private User sessionUser;

	@Autowired
	public ExploreEventPresenter(SportEventService sportEventService,
			UserService userService) {
		this.sportEventService = sportEventService;
		this.userService = userService;
	}

	public void join(SportEvent sportEvent) {
		try {
			sportEventService.joinToSportEvent(sportEvent, sessionUser);
			getView().showInfoNotification("Join was successful");
		}
		catch (AlreadyJoinedException e) {
			getView().showErrorNotification(e.getMessage());
		}
		catch (NoEmptyPlaceException e) {
			getView().showErrorNotification(e.getMessage());
		}
	}

	public void leave(SportEvent sportEvent) {
		try {
			sportEventService.leaveFromSportEvent(sportEvent, sessionUser);
			getView().showInfoNotification("Leave was successful");
		}
		catch (NotParticipantException e) {
			getView().showErrorNotification(e.getMessage());
		}
	}

	public User getSessionUser() {
		return sessionUser;
	}

	public void updateGridDate() {
		getView().setGridItems(sportEventService.findAll());
	}

	public void enter() {
		sessionUser = userService.getCurrentUser();
		updateGridDate();
	}

}
