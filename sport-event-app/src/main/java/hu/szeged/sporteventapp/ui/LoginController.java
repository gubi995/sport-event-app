package hu.szeged.sporteventapp.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

import com.vaadin.spring.annotation.SpringComponent;

import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.events.LoginEvent;
import hu.szeged.sporteventapp.ui.listeners.LoginListener;

@PrototypeScope
@SpringComponent
public class LoginController implements LoginListener {

	private final VaadinSecurity vaadinSecurity;
	private final EventBus.SessionEventBus eventBus;
	private final LoginScreen loginScreen;
	private final UserService userService;

	@Autowired
	public LoginController(LoginScreen loginScreen, UserService userService,
			EventBus.SessionEventBus eventBus, VaadinSecurity vaadinSecurity) {
		this.loginScreen = loginScreen;
		this.userService = userService;
		this.eventBus = eventBus;
		this.vaadinSecurity = vaadinSecurity;
		eventBus.subscribe(this);
	}

	public LoginScreen getLoginScreen() {
		return loginScreen;
	}

	@EventBusListenerMethod
	public void onLogin(LoginEvent params) {
		String username = params.getLoginParameter("username");
		String password = params.getLoginParameter("password");

		try {
			final Authentication authentication = vaadinSecurity.login(username,
					password);
			eventBus.publish(this,
					new SuccessfulLoginEvent(loginScreen.getUI(), authentication));
		}
		catch (AuthenticationException e) {
			loginScreen.loginFailed(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			eventBus.unsubscribe(this);
		}
	}

	// @EventBusListenerMethod
	// public void onRegistration(RegistrationEvent params) {
	// String username = params.getLoginParameter("username");
	// String email = params.getLoginParameter("email");
	// String password = params.getLoginParameter("password");
	// String passwordForValidation = params.getLoginParameter("password");
	//
	// // TODO
	// }
}
