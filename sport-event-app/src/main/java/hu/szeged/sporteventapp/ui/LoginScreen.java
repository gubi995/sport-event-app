package hu.szeged.sporteventapp.ui;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.ui.events.LoginEvent;
import hu.szeged.sporteventapp.ui.events.RegistrationEvent;
import hu.szeged.sporteventapp.ui.validator.PasswordValidator;

@UIScope
@SpringComponent
public class LoginScreen extends CustomComponent {

	private final EventBus.UIEventBus eventBus;

	private TextField userNameFieldForLogin;
	private TextField userNameFieldForRegister;
	private TextField emailFieldForRegister;
	private PasswordField passwordFieldForLogin;
	private PasswordField passwordFieldForRegister;
	private PasswordField passwordFieldForValidate;
	private Button loginButton;
	private Button registerButton;
	private Label infoLabelForLogin;
	private Label infoLabelForRegister;

	Binder<User> userBinder;
	private User user;

	@Autowired
	public LoginScreen(EventBus.UIEventBus eventBus) {
		this.eventBus = eventBus;
		initLayout();
	}

	private void initLayout() {
		HorizontalLayout root = new HorizontalLayout();
		Panel panel = new Panel();
		TabSheet tabSheet = new TabSheet();
		tabSheet.addTab(createLoginLayout(), LOGIN);
		tabSheet.addTab(createRegisterLayout(), REGISTER);
		tabSheet.addSelectedTabChangeListener(
				selectedTabChangeEvent -> settingInfoLabelAsDefault());
		panel.setWidthUndefined();
		panel.setContent(tabSheet);

		root.setSizeFull();
		root.addComponent(panel);
		root.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

		setCompositionRoot(root);
		setSizeFull();

		bindUserAndFields();
	}

	private FormLayout createRegisterLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		infoLabelForRegister = new Label();
		infoLabelForRegister.setVisible(false);
		userNameFieldForRegister = new TextField(USERNAME);
		emailFieldForRegister = new TextField(EMAIL);
		passwordFieldForRegister = new PasswordField(PASSWORD);
		passwordFieldForValidate = new PasswordField(PASSWORD_AGAIN);
		registerButton = new Button(SIGN_UP);
		registerButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		registerButton.addClickListener((Button.ClickListener) event -> register());
		formLayout.addComponents(infoLabelForRegister, userNameFieldForRegister,
				emailFieldForRegister, passwordFieldForRegister, passwordFieldForValidate,
				registerButton);
		setFieldsAsRequired(userNameFieldForRegister, emailFieldForRegister,
				passwordFieldForRegister, passwordFieldForValidate);
		return formLayout;
	}

	private FormLayout createLoginLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		infoLabelForLogin = new Label();
		infoLabelForLogin.setVisible(false);
		userNameFieldForLogin = new TextField(USERNAME);
		passwordFieldForLogin = new PasswordField(PASSWORD);
		loginButton = new Button(SIGN_IN);
		loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		loginButton.addClickListener((Button.ClickListener) event -> login());
		formLayout.addComponents(infoLabelForLogin, userNameFieldForLogin,
				passwordFieldForLogin, loginButton);
		return formLayout;
	}

	private void login() {
		user.setUsername(userNameFieldForLogin.getValue());
		user.setPassword(passwordFieldForLogin.getValue());
		eventBus.publish(this, new LoginEvent(this, user));
	}

	private void register() {
		settingInfoLabelAsDefault();
		user.setUsername(userNameFieldForRegister.getValue());
		user.setEmail(emailFieldForRegister.getValue());
		user.setPassword(passwordFieldForRegister.getValue());
		if (user.getPassword().equals(passwordFieldForValidate.getValue())
				&& userBinder.isValid()) {
			eventBus.publish(this, new RegistrationEvent(this, user));
		}
		else {
			adjustRegisterLabel("The passwords are not same", ValoTheme.LABEL_FAILURE,
					true);
		}
	}

	private void setFieldsAsRequired(TextField... fields) {
		Arrays.stream(fields)
				.forEach(textField -> textField.setRequiredIndicatorVisible(true));
	}

	private void bindUserAndFields() {
		userBinder = new Binder<>();
		user = new User();
		userBinder.setBean(user);
		userBinder.forField(userNameFieldForRegister)
				.asRequired("Every User must have username")
				.bind(User::getUsername, User::setUsername);
		userBinder.forField(passwordFieldForRegister)
				.withValidator(new PasswordValidator())
				.bind(User::getPassword, User::setPassword);
		userBinder.forField(emailFieldForRegister)
				.withValidator(new EmailValidator(
						"This doesn't look like a valid email address"))
				.bind(User::getEmail, User::setEmail);
	}

	public void clearAllRegisterRelatedFieldValue() {
		userNameFieldForRegister.clear();
		emailFieldForRegister.clear();
		passwordFieldForRegister.clear();
		passwordFieldForValidate.clear();

	}

	public void setLoggedOut(boolean loggedOut) {
		infoLabelForLogin.addStyleName(ValoTheme.LABEL_SUCCESS);
		infoLabelForLogin.setValue("Logout was successful. See you later :)");
		infoLabelForLogin.setVisible(loggedOut);
	}

	public void loginFailed(String message) {
		userNameFieldForLogin.focus();
		userNameFieldForLogin.selectAll();
		infoLabelForLogin.setValue(String.format("Login failed: %s", message));
		infoLabelForLogin.setStyleName(ValoTheme.LABEL_FAILURE);
		infoLabelForLogin.setVisible(true);
	}

	public void adjustRegisterLabel(String message, String style, boolean visible) {
		userNameFieldForRegister.focus();
		userNameFieldForRegister.selectAll();
		infoLabelForRegister.setValue(message);
		infoLabelForRegister.setStyleName(style);
		infoLabelForRegister.setVisible(visible);
	}

	public void settingInfoLabelAsDefault() {
		infoLabelForLogin.setValue("");
		infoLabelForLogin.setVisible(false);
		infoLabelForRegister.setValue("");
		infoLabelForRegister.setVisible(false);
	}

}
