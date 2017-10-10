package hu.szeged.sporteventapp.ui.custom_components;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.SportEventService;
import hu.szeged.sporteventapp.backend.service.UserService;

@UIScope
@SpringComponent
public class MessageBoardPresenter {

	private final SportEventService sportEventService;
	private final UserService userService;
	private MessageBoardForm messageBoardForm;
	private SportEvent currentSportEvent;
	private User sessionUser;

	@Autowired
	public MessageBoardPresenter(SportEventService sportEventService, UserService userService) {
		this.sportEventService = sportEventService;
		this.userService = userService;
		initSessionUser();
	}

	private void initSessionUser() {
		sessionUser = userService.getCurrentUser();
	}

	public MessageBoardForm getMessageBoardForm() {
		return messageBoardForm;
	}

	public void setMessageBoardForm(MessageBoardForm messageBoardForm) {
		this.messageBoardForm = messageBoardForm;
	}

	public void setCurrentSportEvent(SportEvent currentSportEvent) {
		this.currentSportEvent = currentSportEvent;
		setMessagesForMessageBoard();
	}

	private void setMessagesForMessageBoard() {
		getMessageBoardForm().setMessagesForMessageBoard(currentSportEvent.getMessageBoard().getMessages());
	}
}
