package hu.szeged.sporteventapp.ui;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.views.AbstractView;

public abstract class AbstractPresenter<V extends AbstractView> implements Serializable {

	protected final UserService userService;
	protected User sessionUser;
	protected V view;

	@Autowired
	public AbstractPresenter(UserService userService) {
		this.userService = userService;
	}

	public void setView(V view) {
		this.view = view;
	}

	public V getView() {
		return view;
	}

	public void enter() {
		sessionUser = userService.getCurrentUser();
	}
}
