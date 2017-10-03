package hu.szeged.sporteventapp.ui.views.eventviews;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;

@UIScope
@SpringComponent
public class SingleEventPresenter extends AbstractPresenter<SingleEventView> {

	@Autowired
	public SingleEventPresenter(UserService userService) {
		super(userService);
	}

	@Override
	public void enter() {
	}
}
