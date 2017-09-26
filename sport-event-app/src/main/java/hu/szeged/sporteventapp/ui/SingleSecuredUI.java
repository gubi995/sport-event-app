package hu.szeged.sporteventapp.ui;

import hu.szeged.sporteventapp.ui.loginscreen.LoginScreen;
import hu.szeged.sporteventapp.ui.loginscreen.LoginScreenPresenter;
import hu.szeged.sporteventapp.ui.mainscreen.MainScreenPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SecurityExceptionUtils;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SpringUI
@Push
@Theme("mytheme")
public class SingleSecuredUI extends UI {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	VaadinSecurity vaadinSecurity;

	@Autowired
	EventBus.UIEventBus eventBus;

	@Autowired
	LoginScreenPresenter loginScreenPresenter;

	@Autowired
	MainScreenPresenter mainScreenPresenter;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Sport Event App");
		// Let's register a custom error handler to make the 'access denied' messages a
		// bit friendlier.
		setErrorHandler(new DefaultErrorHandler() {
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				if (SecurityExceptionUtils
						.isAccessDeniedException(event.getThrowable())) {
					Notification.show("Sorry, you don't have access to do that.");
				}
				else {
					super.error(event);
				}
			}
		});
		if (vaadinSecurity.isAuthenticated()) {
			showMainScreen();
		}
		else {
			showLoginScreen(request.getParameter("logout") != null);
		}
	}

	@Override
	public void attach() {
		super.attach();
		eventBus.subscribe(this);
	}

	@Override
	public void detach() {
		eventBus.unsubscribe(this);
		super.detach();
	}

	private void showLoginScreen(boolean loggedOut) {
		LoginScreen loginScreen = loginScreenPresenter.getLoginScreen();
		loginScreen.setLoggedOut(loggedOut);
		setContent(loginScreen);
	}

	private void showMainScreen() {
		mainScreenPresenter.getView().initNavigator();
		setContent(mainScreenPresenter.getView());
	}

	@EventBusListenerMethod
	void onLogin(SuccessfulLoginEvent loginEvent) {
		if (loginEvent.getSource().equals(this)) {
			access(() -> showMainScreen());
		}
		else {
			getPage().reload();
		}
	}
}
