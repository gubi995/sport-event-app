package hu.szeged.sporteventapp.ui.custom_components;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.*;
import hu.szeged.sporteventapp.backend.repositories.MessageRepository;
import hu.szeged.sporteventapp.backend.service.EmailService;
import hu.szeged.sporteventapp.backend.service.UserService;

@UIScope
@SpringComponent
public class MessageBoardPresenter implements Serializable {

	private final UserService userService;
	private final MessageRepository messageRepository;
	private final EmailService emailService;
	private MessageBoardForm messageBoardForm;
	private MessageBoard currentMessageBoard;
	private User sessionUser;

	@Autowired
	public MessageBoardPresenter(UserService userService, MessageRepository messageRepository,
			EmailService emailService) {
		this.userService = userService;
		this.messageRepository = messageRepository;
		this.emailService = emailService;
		initSessionUser();
	}

	private void initSessionUser() {
		sessionUser = userService.getCurrentUser();
	}

	private void setMessagesForMessageBoard() {
		Set<Message> messages = currentMessageBoard.getMessages();
		getMessageBoardForm().setMessagesForMessageBoard(sortMessagesByDateTime(messages));
	}

	private List<Message> sortMessagesByDateTime(Collection<Message> messages) {
		return messages.stream().sorted(Comparator.comparing(AbstractEntity::getCreatedAt))
				.collect(Collectors.toList());
	}

	public User getSessionUser() {
		return sessionUser;
	}

	public void setCurrentMessageBoard(MessageBoard messageBoard) {
		this.currentMessageBoard = messageBoard;
		setMessagesForMessageBoard();
	}

	public List<Message> refreshMessagesForMessageBoard() {
		List<Message> messages = messageRepository.findAll();
		return sortMessagesByDateTime(messages);
	}

	public void saveMassage(Message message) {
		messageRepository.save(message);
	}

	public MessageBoardForm getMessageBoardForm() {
		return messageBoardForm;
	}

	public MessageBoard getMessageBoard() {
		return currentMessageBoard;
	}

	public void setMessageBoardForm(MessageBoardForm messageBoardForm) {
		this.messageBoardForm = messageBoardForm;
	}

	public void deleteMessage(Message message) {
		messageRepository.delete(message);
	}

	public void sendCircular(String postText, SportEvent sportEvent, String subject) {
		sportEvent.getParticipants().stream()
				.forEach(user -> emailService.sendSimpleMessage(user.getEmail(), subject, postText + template()));
	}

	private String template() {
		return String.format(
				"\n\nThe Email sent over Sport Community App\nThe sender username: %s, email: %s\n"
						+ "\nBest regards,\nSport Community App\n\nPlease don't reply for this email",
				sessionUser.getUsername(), sessionUser.getEmail());
	}
}
