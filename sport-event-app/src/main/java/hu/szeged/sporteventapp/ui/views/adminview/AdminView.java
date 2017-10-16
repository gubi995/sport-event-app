package hu.szeged.sporteventapp.ui.views.adminview;

import static hu.szeged.sporteventapp.ui.views.adminview.AdminView.VIEW_NAME;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;

import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.custom_components.ParticipantForm;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "admin")
@Secured("ADMIN")
@SideBarItem(sectionId = Sections.ADMIN, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.COFFEE)
public class AdminView extends AbstractView {

	public static final String VIEW_NAME = "Admin panel";

	private final AdminPresenter presenter;
	private final ParticipantForm participantForm;

	@Autowired
	public AdminView(AdminPresenter presenter, ParticipantForm participantForm) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.participantForm = participantForm;
	}

	@Override
	public void initComponent() {

	}

	@Override
	public void initBody() {

	}

	@PostConstruct
	public void init() {
		presenter.setView(this);
		participantForm.constructParticipantFormInAdminMode(Optional.ofNullable(presenter.getUsers()));
		addComponentsAndExpand(participantForm);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		presenter.enter();
	}
}
