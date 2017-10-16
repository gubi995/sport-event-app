package hu.szeged.sporteventapp.ui.custom_components;

import java.io.Serializable;

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

	public void delete(User user) {
		userService.delete(user);
	}

	public void updateGridData() {
		// getForm().setParticipants(userService.findAll());
	}
}
