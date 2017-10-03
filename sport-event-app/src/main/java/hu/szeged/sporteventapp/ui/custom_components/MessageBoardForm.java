package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.MESSAGE_BOARD;
import static hu.szeged.sporteventapp.ui.constants.ViewConstants.POST;

import java.util.List;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.Message;
import hu.szeged.sporteventapp.common.util.ImageUtil;
import hu.szeged.sporteventapp.ui.views.INotifier;

import javax.annotation.PostConstruct;

@ViewScope
@SpringComponent
public class MessageBoardForm extends MVerticalLayout implements INotifier {

	private static final String MB_STYLE_NAME = "message-board";

	private final MessageBoardPresenter presenter;

	private TextArea textArea;
	private MessageHolder messageHolder;
	private Button postButton;
	private Button refreshButton;

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
		postButton.addClickListener(e -> {
			addPost(textArea.getValue());
			textArea.clear();
		});
		refreshButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
	}

	private void buildContent() {
		addComponentsAndExpand(messageHolder);
		add(textArea);
		add(new MHorizontalLayout().add(refreshButton, postButton))
				.alignAll(Alignment.MIDDLE_RIGHT);
	}

    @PostConstruct
	private void initPresenter(){
        presenter.setMessageBoardForm(this);
    }

	private void setEnableStateOfPostButton() {
		if (textArea.getValue().length() > 0) {
			postButton.setEnabled(true);
		}
		else {
			postButton.setEnabled(false);
		}
	}

	public void setMessagesForMessageBoard(List<Message> messages) {
		messageHolder.generatePosts(messages);
	}

	public void addPost(String message) {
		messageHolder.addPost(message);
	}

	public void setCurrentEvent(SportEvent sportEvent){
	    presenter.setCurrentSportEvent(sportEvent);
    }

    private class MessageHolder extends Panel {

		private VerticalLayout contentLayout;

		public MessageHolder(String caption) {
			setCaption(caption);
			contentLayout = new VerticalLayout();
			setContent(contentLayout);
		}

		public void addPost(String message) {
			contentLayout.addComponent(new Post(new Message(message, null, null)));
		}

		public void generatePosts(List<Message> messages) {

		}
	}

	private class Post extends HorizontalLayout {

		private static final String P_STYLE_NAME = "post";
		private final Message message;

		public Post(Message message) {
			this.setStyleName(P_STYLE_NAME);
			this.setWidth(100, Unit.PERCENTAGE);
			this.message = message;
			createPost();
		}

		private void createPost() {
			Image image = createImage();
			Label label = createMassageBox();
			label.setSizeFull();
			this.addComponents(image, label);
			this.setExpandRatio(image, 0.1f);
			this.setExpandRatio(label, 0.9f);
			this.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		}

		private Image createImage() {
			Image image = new Image();
			image.setCaption("Caption");
			image.setSource(ImageUtil.setImageThemeResource("default-user.png"));
			return image;
		}

		private Label createMassageBox() {
			return new Label(message.getText());
		}
	}
}
