package hu.szeged.sporteventapp.ui.views.adminview;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.data.enums.Role;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;

@UIScope
@SpringComponent
public class AdminPresenter extends AbstractPresenter<AdminView> {

	@Autowired
	public AdminPresenter(UserService userService) {
		super(userService);
	}

	@Override
	public void enter() {
		super.enter();
	}

	public Set<User> getUsers() {
		return getUsersWithoutAdmins(userService.findAll());
	}

	private Set<User> getUsersWithoutAdmins(Set<User> users) {
		return users.stream().filter(user -> user.getRole().equals(Role.USER)).collect(Collectors.toSet());
	}
}
