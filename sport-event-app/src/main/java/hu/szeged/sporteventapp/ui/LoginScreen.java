package hu.szeged.sporteventapp.ui;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.ui.events.LoginEvent;
import hu.szeged.sporteventapp.ui.events.RegistrationEvent;

@PrototypeScope
@SpringComponent
public class LoginScreen extends CustomComponent {

	private final EventBus.SessionEventBus eventBus;

	private TextField userNameFieldForLogin;
	private TextField userNameFieldForRegister;
	private TextField emailFieldForRegister;
	private PasswordField passwordFieldForLogin;
	private PasswordField passwordFieldForRegister;
	private PasswordField passwordFieldForValidate;
	private Button loginButton;
	private Button registerButton;
	private Label infoLabel;

	@Autowired
	public LoginScreen(EventBus.SessionEventBus eventBus) {
		this.eventBus = eventBus;
		initLayout();
	}

	public void setLoggedOut(boolean loggedOut) {
		infoLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
		infoLabel.setVisible(loggedOut);
	}

	private void initLayout() {
		HorizontalLayout root = new HorizontalLayout();
		Panel panel = new Panel();
		TabSheet tabSheet = new TabSheet();
		tabSheet.addTab(createLoginLayout(), LOGIN);
		tabSheet.addTab(createRegisterLayout(), REGISTER);
		panel.setWidthUndefined();
		panel.setContent(tabSheet);

		root.setSizeFull();
		root.addComponent(panel);
		root.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

		setCompositionRoot(root);
		setSizeFull();
	}

	private FormLayout createRegisterLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		userNameFieldForRegister = new TextField(USERNAME);
		emailFieldForRegister = new TextField(EMAIL);
		passwordFieldForRegister = new PasswordField(PASSWORD);
		passwordFieldForValidate = new PasswordField(PASSWORD_AGAIN);
		registerButton = new Button(SIGN_UP);
		registerButton.setDisableOnClick(true);
		registerButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		registerButton.addClickListener((Button.ClickListener) event -> register());
		formLayout.addComponents(userNameFieldForRegister, emailFieldForRegister,
				passwordFieldForRegister, passwordFieldForValidate, registerButton);
		return formLayout;
	}

	private FormLayout createLoginLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		infoLabel = new Label();
		infoLabel.setVisible(false);
		userNameFieldForLogin = new TextField(USERNAME);
		passwordFieldForLogin = new PasswordField(PASSWORD);
		loginButton = new Button(SIGN_IN);
		loginButton.setDisableOnClick(true);
		loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		loginButton.addClickListener((Button.ClickListener) event -> login());
		formLayout.addComponents(infoLabel, userNameFieldForLogin, passwordFieldForLogin,
				loginButton);
		return formLayout;
	}

	private void login() {
		HashMap<String, String> params = new HashMap();
		params.put("username", userNameFieldForLogin.getValue());
		params.put("password", passwordFieldForLogin.getValue());
		eventBus.publish(this, new LoginEvent(this, params));
	}

	private void register() {
		HashMap<String, String> params = new HashMap();
		params.put("username", userNameFieldForRegister.getValue());
		params.put("email", emailFieldForRegister.getValue());
		params.put("password", passwordFieldForRegister.getValue());
		params.put("passwordForValidate", passwordFieldForValidate.getValue());
		eventBus.publish(this, new RegistrationEvent(this, params));
	}

	public void loginFailed(String message) {
		userNameFieldForLogin.focus();
		userNameFieldForLogin.selectAll();
		infoLabel.setValue(String.format("Login failed: %s", message));
		infoLabel.setStyleName(ValoTheme.LABEL_FAILURE);
		infoLabel.setVisible(true);
	}

}
