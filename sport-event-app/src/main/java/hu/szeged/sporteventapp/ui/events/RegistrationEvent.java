package hu.szeged.sporteventapp.ui.events;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import hu.szeged.sporteventapp.ui.LoginScreen;

public class RegistrationEvent extends ApplicationEvent {
	private final Map<String, String> params;

	public RegistrationEvent(LoginScreen source, Map<String, String> params) {
		super(source);
		this.params = params;
	}

	public LoginScreen getSource() {
		return (LoginScreen) super.getSource();
	}

	public String getLoginParameter(String name) {
		return (String) this.params.get(name);
	}
}