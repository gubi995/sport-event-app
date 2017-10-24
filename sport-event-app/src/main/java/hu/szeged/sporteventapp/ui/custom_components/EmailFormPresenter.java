package hu.szeged.sporteventapp.ui.custom_components;

import java.io.Serializable;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.EmailService;
import hu.szeged.sporteventapp.backend.service.UserService;

@UIScope
@SpringComponent
public class EmailFormPresenter implements Serializable {

	private final EmailService emailService;
	private final UserService userService;
	private User sessionUser;

	public EmailFormPresenter(EmailService emailService, UserService userService) {
		this.emailService = emailService;
		this.userService = userService;
		initSessionUser();
	}

	private void initSessionUser() {
		sessionUser = userService.getCurrentUser();
	}

	private String template() {
		return String.format(
				"\n\nThe Email sent over Sport Community App\nThe sender username: %s, email: %s\n"
						+ "\nBest regards,\nSport Community App\n\nPlease don't reply for this email",
				sessionUser.getUsername(), sessionUser.getEmail());
	}

	public void sendEmail(String to, String subject, String text) {
		emailService.sendSimpleMessage(to, subject, text + template());
	}
}
