package hu.szeged.sporteventapp.ui.views.userprofile;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;
import static hu.szeged.sporteventapp.ui.views.userprofile.UserProfileView.VIEW_NAME;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.util.ValidatorUtil;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.validator.PasswordValidator;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "profile")
@SideBarItem(sectionId = Sections.PROFILE, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.USER_CARD)
public class UserProfileView extends AbstractView implements View, Serializable {

	public static final String VIEW_NAME = "My profile";

	private final UserProfilePresenter presenter;

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
	private Upload pictureUpload;
	private Image userImage;
	private ImageUploader imageUploader;

	@Autowired
	public UserProfileView(UserProfilePresenter presenter) {
		super(VIEW_NAME);
		this.presenter = presenter;
	}

	@Override
	public void initComponent() {
		imageUploader = new ImageUploader();
		usernameField = new TextField(USERNAME);
		emailField = new TextField(EMAIL);
		ageField = new TextField(AGE);
		realNameField = new TextField(REAL_NAME);
		mobileField = new TextField(MOBILE);
		updateButton = new Button(UPDATE);
		updateButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		oldPasswordField = new PasswordField(OLD + PASSWORD);
		newPasswordField = new PasswordField(NEW + PASSWORD);
		validateField = new PasswordField(NEW + PASSWORD_AGAIN);
		changePasswordButton = new Button(CHANGE);
		changePasswordButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		userImage = new Image();
		pictureUpload = new Upload();

		binder = new BeanValidationBinder<>(User.class);
	}

	@Override
	public void initBody() {
		addComponent(new MHorizontalLayout(
				new MVerticalLayout(createPasswordChangeGroup(),
						createOptionFieldsGroup()).alignAll(Alignment.MIDDLE_RIGHT),
				new MVerticalLayout(createImageGroup(), createReadOnlyGroup())
						.alignAll(Alignment.MIDDLE_RIGHT)).withWidth(85,
								Unit.PERCENTAGE));
	}

	@PostConstruct
	public void init() {
		presenter.setView(this);
		initPicutreUploader();

		changePasswordButton.addClickListener(
				clickEvent -> presenter.changePassword(getPasswordRelevantData()));
		updateButton.addClickListener(clickEvent -> {
			presenter.updateUserData(getUpdateRelevantData());
		});
	}

	public void initBinder(User user) {
		binder.setBean(user);
		binder.setRequiredConfigurator(null);
		binder.forField(usernameField).bind(User::getUsername, User::setUsername);
		binder.forField(emailField).bind(User::getEmail, User::setEmail);
		binder.forField(ageField).withConverter(Integer::parseInt, String::valueOf)
				.bind(User::getAge, User::setAge);
		binder.forField(realNameField).bind(User::getRealName, User::setRealName);
		binder.forField(mobileField).bind(User::getMobileNumber, User::setMobileNumber);
		ValidatorUtil.addValidator(changePasswordButton, new PasswordValidator(),
				newPasswordField);
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

	private void initPicutreUploader() {
		pictureUpload.setReceiver(imageUploader);
		pictureUpload.addSucceededListener(imageUploader);
		pictureUpload.addStartedListener(e -> {
			if (!e.getMIMEType().equals("image/png")
					&& !e.getMIMEType().equals("image/jpeg")) {
				showErrorNotification("Upload interrupted! It is not an image");
				pictureUpload.interruptUpload();
			}
		});
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
		layout.addComponents(oldPasswordField, newPasswordField, validateField,
				changePasswordButton);
		return layout;
	}

	private VerticalLayout createImageGroup() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();
		layout.addStyleName("user-image-group");
		layout.setSpacing(false);
		userImage.addStyleName("user-image");
		userImage.setHeight(200, Unit.PIXELS);
		userImage.setWidth(200, Unit.PIXELS);
		layout.addComponents(userImage, pictureUpload);
		layout.setComponentAlignment(userImage, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(pictureUpload, Alignment.MIDDLE_CENTER);
		return layout;
	}

	private class ImageUploader implements Upload.Receiver, Upload.SucceededListener {
		private File file;
		private String filename;

		public OutputStream receiveUpload(String filename, String mimeType) {

			try {
				this.filename = filename;
				String webappPath = VaadinService.getCurrent().getBaseDirectory()
						.getAbsolutePath();
				file = new File(webappPath + "/images/" + filename);
				FileOutputStream fos = new FileOutputStream(file);
				return fos;
			}
			catch (final java.io.FileNotFoundException e) {
				showErrorNotification(e.getMessage());
				return null;
			}
		}

		@Override
		public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
			userImage.setSource(new FileResource(file));
			presenter.updateUserImageData(filename);
		}
	}
}
