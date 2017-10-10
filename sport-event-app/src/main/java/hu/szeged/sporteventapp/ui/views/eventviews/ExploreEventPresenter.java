package hu.szeged.sporteventapp.ui.views.eventviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

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

	private final SportEventService sportEventService;

	@Autowired
	public ExploreEventPresenter(SportEventService sportEventService, UserService userService) {
		super(userService);
		this.sportEventService = sportEventService;
	}

	public void join(SportEvent sportEvent) {
		try {
			sportEventService.joinToSportEvent(sportEvent, sessionUser);
			getView().showInfoNotification("Join was successful");
		} catch (AlreadyJoinedException e) {
			getView().showWarningNotification(e.getMessage());
		} catch (NoEmptyPlaceException e) {
			getView().showErrorNotification(e.getMessage());
		} catch (ObjectOptimisticLockingFailureException e2) {
			getView().showErrorNotification("Please wait a minute before leave");
		}
	}

	public void leave(SportEvent sportEvent) {
		try {
			sportEventService.leaveFromSportEvent(sportEvent, sessionUser);
			getView().showInfoNotification("Leave was successful");
		} catch (NotParticipantException e) {
			getView().showErrorNotification(e.getMessage());
		} catch (ObjectOptimisticLockingFailureException e2) {
			getView().showErrorNotification("Please wait a minute before leave");
		}
	}

	public User getSessionUser() {
		return sessionUser;
	}

	public void updateGridDate() {
		getView().setGridItems(sportEventService.findAll());
	}

	@Override
	public void enter() {
		super.enter();
		updateGridDate();
	}
}
