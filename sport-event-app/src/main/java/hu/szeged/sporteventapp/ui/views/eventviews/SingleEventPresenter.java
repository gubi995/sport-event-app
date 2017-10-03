package hu.szeged.sporteventapp.ui.views.eventviews;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.service.SportEventService;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;

@UIScope
@SpringComponent
public class SingleEventPresenter extends AbstractPresenter<SingleEventView> {

	private final SportEventService sportEventService;

	@Autowired
	public SingleEventPresenter(UserService userService,
			SportEventService sportEventService) {
		super(userService);
		this.sportEventService = sportEventService;
	}

	private void updateEvent(SportEvent sportEvent){
		sportEventService.save(sportEvent);
	}

	private void updateData() {
	}

	@Override
	public void enter() {
		super.enter();
		updateData();
	}
}
