package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.List;

import javax.annotation.PostConstruct;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import de.steinwedel.messagebox.MessageBox;
import hu.szeged.sporteventapp.backend.data.entity.Message;
import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.util.ResourceUtil;
import hu.szeged.sporteventapp.ui.views.INotifier;

@SpringComponent
@ViewScope
public class MessageBoardForm extends MVerticalLayout implements INotifier {

	private static final String MB_STYLE_NAME = "message-board";

	private final MessageBoardPresenter presenter;

	private TextArea textArea;
	private MessageHolder messageHolder;
	private Button postButton;
	private Button refreshButton;
	private Button circularButton;

	private Binder<SportEvent> binder;

	@Autowired
	public MessageBoardForm(MessageBoardPresenter presenter) {
		this.presenter = presenter;
		initComponents();
		buildContent();
		setSizeFull();
		addStyleName(MB_STYLE_NAME);
	}

	private void initComponents() {
		messageHolder = new MessageHolder(MESSAGE_BOARD);
		textArea = new TextArea();
		postButton = new Button(POST);
		refreshButton = new Button(VaadinIcons.REFRESH);
		circularButton = new Button(CIRCULAR);
		binder = new Binder<>(SportEvent.class);
		initTextArea();
		initButton();
	}

	private void initTextArea() {
		textArea.setPlaceholder("Write your message...");
		textArea.setWidth(100, Unit.PERCENTAGE);
		textArea.addValueChangeListener(v -> setEnableStateOfPostButton());
	}

	private void initButton() {
		postButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		postButton.setIcon(VaadinIcons.PLAY_CIRCLE_O);
		postButton.setEnabled(false);
		postButton.addClickListener(e -> sendPost());
		refreshButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		refreshButton.addClickListener(e -> refreshMessages());
		circularButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		circularButton.setIcon(VaadinIcons.ENVELOPE_O);
		circularButton.setEnabled(false);
		circularButton.addClickListener(e -> {
			TextField input = new TextField("Do you really want to send the following post as circular?<br/>" +
					"If yes then please give the subject");
			input.setCaptionAsHtml(true);

			MessageBox.createQuestion()
					.withCaption("Circular")
					.withMessage(input)
					.withOkButton(() -> sendCircular(input.getValue()))
					.withCancelButton()
					.open();
		});
	}

	private void sendPost() {
		addPost(textArea.getValue());
		textArea.clear();
	}

	private void sendCircular(String subject) {
		String postText = textArea.getValue();
		addPost(postText);
		presenter.sendCircular(postText, binder.getBean(), Strings.nullToEmpty(subject));
		textArea.clear();
	}

	private void refreshMessages() {
		messageHolder.clearMessages();
		messageHolder.generatePosts(presenter.refreshMessagesForMessageBoard());
	}

	private void buildContent() {
		addComponentsAndExpand(messageHolder);
		add(textArea);
		add(new MHorizontalLayout().add(refreshButton, postButton, circularButton)).alignAll(Alignment.MIDDLE_RIGHT);
	}

	@PostConstruct
	private void initPresenter() {
		presenter.setMessageBoardForm(this);
	}

	private void setEnableStateOfPostButton() {
		if (textArea.getValue().length() > 0 && textArea.getValue().length() < 255) {
			circularButton.setEnabled(true);
			postButton.setEnabled(true);
			postButton.setDescription(null);
		} else {
			circularButton.setEnabled(false);
			postButton.setEnabled(false);
			postButton.setDescription("The length of message size must(min:1, max:254) character");
		}
	}

	public void setMessagesForMessageBoard(List<Message> messages) {
		messageHolder.generatePosts(messages);
	}

	public void addPost(String message) {
		messageHolder.addPost(message);
	}

	public void setSportEvent(SportEvent sportEvent) {
		binder.setBean(sportEvent);
		presenter.setCurrentMessageBoard(sportEvent.getMessageBoard());
	}

	private class MessageHolder extends Panel {

		private VerticalLayout contentLayout;

		public MessageHolder(String caption) {
			setCaption(caption);
			contentLayout = new VerticalLayout();
			setContent(contentLayout);
		}

		public void addPost(String message) {
			Message msg = new Message(message, presenter.getMessageBoard(), presenter.getSessionUser());
			presenter.saveMassage(msg);
			contentLayout.addComponent(new Post(msg, true));
		}

		public void generatePosts(List<Message> messages) {
			User user = presenter.getSessionUser();
			for (Message message : messages) {
				boolean messageIsMine = message.getUser().equals(user);
				contentLayout.addComponent(new Post(message, messageIsMine));
			}
		}

		public void clearMessages() {
			contentLayout.removeAllComponents();
		}

		public void deletePost(Post post) {
			this.contentLayout.removeComponent(post);
		}
	}

	private class Post extends HorizontalLayout {

		private static final String P_STYLE_NAME = "post";
		private final Message message;

		public Post(Message message, boolean messageIsMine) {
			this.setStyleName(P_STYLE_NAME);
			this.setWidth(100, Unit.PERCENTAGE);
			this.message = message;
			createPost(messageIsMine);
		}

		private void createPost(boolean messageIsMine) {
			Image image = createImage();
			Label label = createMessageBox();
			label.setSizeFull();
			this.addComponents(image, label);
			this.setExpandRatio(image, 0.1f);
			this.setExpandRatio(label, 0.8f);
			this.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
			adjustDeleteButton(messageIsMine);
		}

		private Image createImage() {
			User user = message.getUser();
			Image image = new Image();
			image.setCaption(user.getUsername());
			image.setSource(ResourceUtil.setUserImageResource(user.getPictureName()));
			return image;
		}

		private Button createDeleteButton() {
			Button deleteButton = new Button();
			deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);
			deleteButton.setIcon(VaadinIcons.CLOSE);
			deleteButton.addClickListener(e -> deletePost());
			return deleteButton;
		}

		private void adjustDeleteButton(boolean messageIsMine) {
			if (messageIsMine) {
				Button deleteButton = createDeleteButton();
				this.addComponent(deleteButton);
				this.setExpandRatio(deleteButton, 0.1f);
				this.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);
			}
		}

		private void deletePost() {
			messageHolder.deletePost(this);
			presenter.deleteMessage(this.message);
		}

		private Label createMessageBox() {
			return new Label(message.getText());
		}
	}
}
