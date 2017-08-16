package hu.szeged.sporteventapp.ui.views;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;
import static hu.szeged.sporteventapp.ui.views.UserProfileView.VIEW_NAME;

import java.io.File;
import java.util.Arrays;

import hu.szeged.sporteventapp.ui.events.LoadUserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.ui.Sections;

@SpringView(name = "profile")
@SideBarItem(sectionId = Sections.PROFILE, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.USER_CARD)
public class UserProfileView extends AbstractView {

	public static final String VIEW_NAME = "My profile";

	private EventBus.UIEventBus eventBus;

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

	@Autowired
	public UserProfileView(EventBus.UIEventBus eventBus) {
		super(VIEW_NAME);
		this.eventBus = eventBus;
	}

	@Override
	public void initBody() {
		HorizontalLayout body = new HorizontalLayout();
		body.setWidth(85, Unit.PERCENTAGE);
		body.setHeightUndefined();
		VerticalLayout rightLayout = new VerticalLayout();
		VerticalLayout leftLayout = new VerticalLayout();

		FormLayout passwordChangeGroup = createPasswordChangeGroup();
		FormLayout optionFieldsGroup = createOptionFieldsGroup();
		leftLayout.addComponents(passwordChangeGroup, optionFieldsGroup);
		setComponentsAlign(leftLayout, Alignment.MIDDLE_RIGHT, passwordChangeGroup,
				optionFieldsGroup);

		VerticalLayout imageGroup = createImageGroup();
		FormLayout readOnlyGroup = createReadOnlyGroup();
		rightLayout.addComponents(imageGroup, readOnlyGroup);
		setComponentsAlign(rightLayout, Alignment.MIDDLE_RIGHT, imageGroup,
				readOnlyGroup);

		body.addComponents(leftLayout, rightLayout);
		addComponent(body);
	}

	private FormLayout createReadOnlyGroup() {
		FormLayout layout = new FormLayout();
		layout.setSizeUndefined();
		usernameField = new TextField(USERNAME);
		usernameField.setEnabled(false);
		emailField = new TextField(EMAIL);
		emailField.setEnabled(false);
		layout.addComponents(usernameField, emailField);
		return layout;
	}

	private FormLayout createPasswordChangeGroup() {
		FormLayout layout = new FormLayout();
		layout.setSizeUndefined();
		oldPasswordField = new PasswordField(OLD + PASSWORD);
		newPasswordField = new PasswordField(NEW + PASSWORD);
		validateField = new PasswordField(NEW + PASSWORD_AGAIN);
		changePasswordButton = new Button(CHANGE);
		layout.addComponents(oldPasswordField, newPasswordField, validateField,
				changePasswordButton);
		return layout;
	}

	private FormLayout createOptionFieldsGroup() {
		FormLayout layout = new FormLayout();
		layout.setSizeUndefined();
		ageField = new TextField(AGE);
		realNameField = new TextField(REAL_NAME);
		mobileField = new TextField(MOBILE);
		updateButton = new Button(UPDATE);
		layout.addComponents(ageField, realNameField, mobileField, updateButton);
		return layout;
	}

	private VerticalLayout createImageGroup() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();
		layout.addStyleName("user-image-group");
		layout.setSpacing(false);
		userImage = new Image();
		userImage.addStyleName("user-image");
		userImage.setSource(setImageResource("default_user_image.png"));
		userImage.setHeight(200, Unit.PIXELS);
		userImage.setWidth(200, Unit.PIXELS);
		pictureUpload = new Upload();
		layout.addComponents(userImage, pictureUpload);
		layout.setComponentAlignment(userImage, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(pictureUpload, Alignment.MIDDLE_CENTER);
		return layout;
	}

	private FileResource setImageResource(String pictureName) {
		String path = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(
				new File(path + "/images/" + pictureName));
		return resource;
	}

	private void setComponentsAlign(AbstractOrderedLayout layout, Alignment alignment,
			AbstractComponent... components) {
		Arrays.stream(components).forEach(abstractComponent -> layout
				.setComponentAlignment(abstractComponent, alignment));
	}

	public void loadingUserData(User user) {
		ageField.setValue(String.valueOf(user.getAge()));
		realNameField.setValue(user.getRealName());
		mobileField.setValue(user.getMobileNumber());
		usernameField.setValue(user.getUsername());
		emailField.setValue(user.getEmail());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		eventBus.publish(this, new LoadUserEvent(this));
	}
}
