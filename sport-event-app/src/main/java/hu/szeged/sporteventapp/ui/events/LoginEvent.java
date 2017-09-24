package hu.szeged.sporteventapp.ui.events;

import org.springframework.context.ApplicationEvent;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.ui.loginscreen.LoginScreen;

public class LoginEvent extends ApplicationEvent {

	private final User user;

	public LoginEvent(LoginScreen source, User user) {
		super(source);
		this.user = user;
	}

	public LoginScreen getSource() {
		return (LoginScreen) super.getSource();
	}

	public User getUser() {
		return user;
	}
}