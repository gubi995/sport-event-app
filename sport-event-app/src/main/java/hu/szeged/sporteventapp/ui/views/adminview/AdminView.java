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
import com.vaadin.spring.annotation.ViewScope;

import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.custom_components.ParticipantFormAdminMode;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "admin")
@Secured("ADMIN")
@SideBarItem(sectionId = Sections.ADMIN, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.COFFEE)
@ViewScope
public class AdminView extends AbstractView {

	public static final String VIEW_NAME = "Admin panel";

	private final AdminPresenter presenter;
	private final ParticipantFormAdminMode form;

	@Autowired
	public AdminView(AdminPresenter presenter, ParticipantFormAdminMode form) {
		super(VIEW_NAME);
		this.presenter = presenter;
		this.form = form;
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
		form.constructParticipantForm(Optional.ofNullable(presenter.getUsers()));
		addComponentsAndExpand(form);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		presenter.enter();
	}
}
