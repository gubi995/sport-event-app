package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.label.MLabel;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.ui.views.INotifier;

@UIScope
@SpringComponent
public class EmailSendForm extends VerticalLayout implements INotifier {

	private static final String CAPTION_STYLE = "email-label";

	private final EmailFormPresenter presenter;

	private TextField subjectField;
	private Label toUsernameLabel;
	private Label toEmailLabel;
	private TextArea textArea;
	private Button sendButton;
	private Button hideButton;

	private User addressee;

	@Autowired
	public EmailSendForm(EmailFormPresenter presenter) {
		super();
		this.presenter = presenter;
		setMargin(false);
		setSizeUndefined();
		initComponent();
		addComponents(new MLabel("Send mail").withStyleName(ValoTheme.LABEL_H2, CAPTION_STYLE).withFullWidth(),
				subjectField, toUsernameLabel, toEmailLabel, textArea, new HorizontalLayout(sendButton, hideButton));
	}

	private void initComponent() {
		subjectField = new TextField(SUBJECT);
		toUsernameLabel = new Label();
		toUsernameLabel.setCaption("Addressee username:");
		toEmailLabel = new Label();
		toEmailLabel.setCaption("Addressee email: ");
		textArea = new TextArea(MESSAGE);
		textArea.setHeight(350, Unit.PIXELS);
		textArea.setWidth(500, Unit.PIXELS);
		sendButton = new Button(SEND);
		sendButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		sendButton.addClickListener(c -> send());
		hideButton = new Button(HIDE);
		hideButton.addClickListener(c -> this.setVisible(false));
	}

	public void setUser(User user) {
		addressee = user;
		toUsernameLabel.setValue(addressee.getUsername());
		toEmailLabel.setValue(addressee.getEmail());
	}

	private void send() {
		Optional<String> subject = subjectField.getOptionalValue();
		Optional<String> text = textArea.getOptionalValue();
		if (subject.isPresent() && text.isPresent()) {
			presenter.sendEmail(addressee.getEmail(), subjectField.getValue(), textArea.getValue());
			showInfoNotification("Message sent successfully");
		} else {
			showWarningNotification("The subject and the message is not be empty");
		}
	}
}