package hu.szeged.sporteventapp.ui.views.userprofile;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;
import static hu.szeged.sporteventapp.ui.custom_components.WrappedUpload.JPG;
import static hu.szeged.sporteventapp.ui.custom_components.WrappedUpload.PNG;
import static hu.szeged.sporteventapp.ui.views.userprofile.UserProfileView.VIEW_NAME;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.util.ResourceUtil;
import hu.szeged.sporteventapp.common.util.ValidatorUtil;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.custom_components.WrappedUpload;
import hu.szeged.sporteventapp.ui.validator.PasswordValidator;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "profile")
@SideBarItem(sectionId = Sections.PROFILE, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.USER_CARD)
@ViewScope
public class UserProfileView extends AbstractView implements Upload.SucceededListener {

	public static final String VIEW_NAME = "My profile";
	private static final String IMAGE_STYLE = "user-image";
	private static final String GROUP_STYLE = "user-image-group";

	private final UserProfilePresenter presenter;
	private final WrappedUpload upload;

	private BeanValidationBinder<User> binder;
	private TextField usernameField;
	private TextField emailField;
	private TextField ageField;
	private TextField realNameField;
	private TextField mobileField;
	private PasswordField oldPasswordField;
	private PasswordField newPasswordField;
	private PasswordField validateField;
	private Button updateButton;
	private Button changePasswordButton;
	private Image userImage;
	private VerticalLayout imageGroupLayout;

	@Autowired
	public UserProfileView(UserProfilePresenter presenter, WrappedUpload upload) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.upload = upload;
	}

	@Override
	public void initComponent() {
		usernameField = new TextField(USERNAME);
		emailField = new TextField(EMAIL);
		ageField = new TextField(AGE);
		realNameField = new TextField(REAL_NAME);
		mobileField = new TextField(MOBILE);
		updateButton = new Button(UPDATE);
		updateButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		updateButton.setWidth(100, Unit.PERCENTAGE);
		oldPasswordField = new PasswordField(OLD + PASSWORD);
		newPasswordField = new PasswordField(NEW + PASSWORD);
		validateField = new PasswordField(NEW + PASSWORD_AGAIN);
		changePasswordButton = new Button(CHANGE);
		changePasswordButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		changePasswordButton.setWidth(100, Unit.PERCENTAGE);
		userImage = new Image();
		imageGroupLayout = createImageGroup();

		binder = new BeanValidationBinder<>(User.class);
	}

	@Override
	public void initBody() {
		addComponent(new MHorizontalLayout(
				new MVerticalLayout(createPasswordChangeGroup(), createOptionFieldsGroup())
						.alignAll(Alignment.MIDDLE_RIGHT),
				new MVerticalLayout(imageGroupLayout, createReadOnlyGroup()).alignAll(Alignment.MIDDLE_RIGHT))
						.withWidth(85, Unit.PERCENTAGE));
	}

	@PostConstruct
	public void init() {
		presenter.setView(this);
		initWrappedUploader();

		changePasswordButton.addClickListener(clickEvent -> presenter.changePassword(getPasswordRelevantData()));
		updateButton.addClickListener(clickEvent -> {
			presenter.updateUserData(getUpdateRelevantData());
		});
	}

	public void initBinder(User user) {
		binder.setBean(user);
		binder.forField(usernameField).bind(User::getUsername, User::setUsername);
		binder.forField(emailField).bind(User::getEmail, User::setEmail);
		binder.forField(ageField).withConverter(Integer::parseInt, String::valueOf).bind(User::getAge, User::setAge);
		binder.forField(realNameField).bind(User::getRealName, User::setRealName);
		binder.forField(mobileField).bind(User::getMobileNumber, User::setMobileNumber);
		ValidatorUtil.addValidator(changePasswordButton, new PasswordValidator(), newPasswordField);
	}

	public Map<String, String> getPasswordRelevantData() {
		HashMap<String, String> data = new HashMap<>();
		data.put("old", oldPasswordField.getValue());
		data.put("new", newPasswordField.getValue());
		data.put("validate", validateField.getValue());
		return data;
	}

	public Map<String, String> getUpdateRelevantData() {
		HashMap<String, String> data = new HashMap<>();
		data.put("age", ageField.getValue());
		data.put("full_name", realNameField.getValue());
		data.put("mobile", mobileField.getValue());
		return data;
	}

	public Image getUserImage() {
		return userImage;
	}

	public void updateBinder(User user) {
		binder.setBean(user);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		presenter.enter();
	}

	private void initWrappedUploader() {
		upload.setContext(ResourceUtil.USER);
		upload.setAllowedMimeType(JPG, PNG);
		upload.getUpload().addSucceededListener(this);
		imageGroupLayout.addComponent(upload);
		imageGroupLayout.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
		String filename = succeededEvent.getFilename();
		userImage.setSource(ResourceUtil.setUserImageResource(filename));
		presenter.updateUserImageData(filename);
	}

	private FormLayout createReadOnlyGroup() {
		FormLayout layout = new FormLayout();
		layout.setSizeUndefined();
		usernameField.setReadOnly(true);
		emailField.setReadOnly(true);
		layout.addComponents(usernameField, emailField);
		return layout;
	}

	private FormLayout createOptionFieldsGroup() {
		FormLayout layout = new FormLayout();
		layout.setSizeUndefined();
		layout.addComponents(ageField, realNameField, mobileField, updateButton);
		return layout;
	}

	private FormLayout createPasswordChangeGroup() {
		FormLayout layout = new FormLayout();
		layout.setSizeUndefined();
		layout.addComponents(oldPasswordField, newPasswordField, validateField, changePasswordButton);
		return layout;
	}

	private VerticalLayout createImageGroup() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();
		layout.addStyleName(GROUP_STYLE);
		layout.setSpacing(false);
		userImage.addStyleName(IMAGE_STYLE);
		userImage.setHeight(200, Unit.PIXELS);
		userImage.setWidth(200, Unit.PIXELS);
		layout.addComponent(userImage);
		layout.setComponentAlignment(userImage, Alignment.MIDDLE_CENTER);
		return layout;
	}

	@Override
	public void detach() {
		upload.getUpload().removeSucceededListener(this);
		super.detach();
	}
}
