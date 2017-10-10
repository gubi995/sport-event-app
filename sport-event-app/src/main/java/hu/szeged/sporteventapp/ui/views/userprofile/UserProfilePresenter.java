package hu.szeged.sporteventapp.ui.views.userprofile;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.common.util.ImageUtil;
import hu.szeged.sporteventapp.ui.AbstractPresenter;

@UIScope
@SpringComponent
public class UserProfilePresenter extends AbstractPresenter<UserProfileView> {

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserProfilePresenter(UserService userService, PasswordEncoder passwordEncoder) {
		super(userService);
		this.passwordEncoder = passwordEncoder;
	}

	public void changePassword(Map<String, String> data) {
		String oldPassword = data.get("old");
		String newPassword = data.get("new");
		String passwordForValidate = data.get("validate");
		if (newPassword.equals(passwordForValidate)
				&& passwordEncoder.matches(oldPassword, sessionUser.getPassword())) {
			sessionUser.setPassword(passwordEncoder.encode(newPassword));
			userService.updateUserPassword(sessionUser.getId(), sessionUser.getPassword());
			getView().updateBinder(sessionUser);
			getView().showInfoNotification("Password change was successful");
		} else {
			getView().showErrorNotification("One of the password is incorrect");
		}
	}

	public void updateUserData(Map<String, String> data) {
		sessionUser.setAge(Integer.parseInt(data.get("age")));
		sessionUser.setRealName(data.get("full_name"));
		sessionUser.setMobileNumber(data.get("mobile"));
		userService.updateUserAdditionalData(sessionUser.getId(), sessionUser.getAge(), sessionUser.getRealName(),
				sessionUser.getMobileNumber());
		getView().updateBinder(sessionUser);
		getView().showInfoNotification("Data update was successful");
	}

	public void updateUserImageData(String pictureName) {
		sessionUser.setPictureName(pictureName);
		userService.updateUserImageData(sessionUser.getId(), pictureName);
		getView().updateBinder(sessionUser);
	}

	@Override
	public void enter() {
		super.enter();
		getView().initBinder(sessionUser);
		getView().getUserImage().setSource(ImageUtil.setImageThemeResource(sessionUser.getPictureName()));
	}
}
