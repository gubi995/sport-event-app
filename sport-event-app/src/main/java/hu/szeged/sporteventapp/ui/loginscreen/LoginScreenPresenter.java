package hu.szeged.sporteventapp.ui.loginscreen;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.DEFAULT_USER_IMAGE;

import hu.szeged.sporteventapp.backend.data.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.common.exception.AlreadyExistsException;
import hu.szeged.sporteventapp.ui.events.LoginEvent;
import hu.szeged.sporteventapp.ui.events.RegistrationEvent;
import hu.szeged.sporteventapp.ui.listeners.LoginEventListener;
import hu.szeged.sporteventapp.ui.listeners.RegistrationEventListener;

@UIScope
@SpringComponent
public class LoginScreenPresenter implements LoginEventListener, RegistrationEventListener {

	private final VaadinSecurity vaadinSecurity;
	private final EventBus.UIEventBus eventBus;
	private final LoginScreen loginScreen;
	private final UserService userService;

	@Autowired
	public LoginScreenPresenter(LoginScreen loginScreen, UserService userService,
								EventBus.UIEventBus eventBus, VaadinSecurity vaadinSecurity) {
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
	public void onLogin(LoginEvent event) {
		try {
			final Authentication authentication = vaadinSecurity
					.login(event.getUser().getUsername(), event.getUser().getPassword());
			eventBus.publish(this,
					new SuccessfulLoginEvent(loginScreen.getUI(), authentication));
			eventBus.unsubscribe(this);
		}
		catch (AuthenticationException e) {
			loginScreen.loginFailed(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EventBusListenerMethod
	public void onRegistration(RegistrationEvent event) {
		try {
			User user = new User(event.getUser().getUsername(),
					event.getUser().getEmail(), event.getUser().getPassword(),
					DEFAULT_USER_IMAGE,
					Role.USER,0,"","", null, null);
			userService.save(user);
			loginScreen.adjustRegisterLabel("Registration successful",
					ValoTheme.LABEL_SUCCESS, true);
			loginScreen.clearAllRegisterRelatedFieldValue();
		}
		catch (TransactionSystemException | AlreadyExistsException e) {
			loginScreen.adjustRegisterLabel(e.getMessage(), ValoTheme.LABEL_FAILURE,
					true);
		}
	}

}
