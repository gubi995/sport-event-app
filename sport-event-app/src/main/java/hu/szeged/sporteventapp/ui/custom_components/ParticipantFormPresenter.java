package hu.szeged.sporteventapp.ui.custom_components;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.EmailService;
import hu.szeged.sporteventapp.backend.service.UserService;

@UIScope
@SpringComponent
public class ParticipantFormPresenter implements Serializable {

	private final UserService userService;
	private final EmailService emailService;
	private ParticipantForm participantForm;
	private User sessionUser;

	@Autowired
	public ParticipantFormPresenter(UserService userService, EmailService emailService) {
		this.userService = userService;
		this.emailService = emailService;
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

	public void setParticipantForm(ParticipantForm participantForm) {
		this.participantForm = participantForm;
	}

	public ParticipantForm getParticipantForm() {
		return participantForm;
	}

	//TODO nem műkszik
	public void delete(User user) {
		userService.delete(user);
	}

	public void updateGridData() {
		getParticipantForm().setParticipants(userService.findAll());
	}

	public void sendEmail(String to, String subject, String text) {
		emailService.sendSimpleMessage(to, subject, text + template());
	}
}
