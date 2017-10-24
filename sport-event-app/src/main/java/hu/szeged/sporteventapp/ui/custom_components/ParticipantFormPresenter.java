package hu.szeged.sporteventapp.ui.custom_components;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.data.enums.Role;
import hu.szeged.sporteventapp.backend.service.UserService;

@UIScope
@SpringComponent
public class ParticipantFormPresenter implements Serializable {

	private final UserService userService;
	private ParticipantForm participantForm;

	@Autowired
	public ParticipantFormPresenter(UserService userService) {
		this.userService = userService;
	}

	private Set<User> getUsersWithoutAdmins(Set<User> users) {
		return users.stream().filter(user -> user.getRole().equals(Role.USER)).collect(Collectors.toSet());
	}

	public Set<User> getUsers() {
		return getUsersWithoutAdmins(userService.findAll());
	}

	public void setParticipantForm(ParticipantForm participantForm) {
		this.participantForm = participantForm;
	}

	public ParticipantForm getParticipantForm() {
		return participantForm;
	}

	public void delete(User user) {
		userService.delete(user);
	}

	public void updateGridData() {
		getParticipantForm().setParticipants(getUsers());
	}
}
