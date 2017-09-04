package hu.szeged.sporteventapp.ui.views.myeventview;

import static hu.szeged.sporteventapp.ui.views.myeventview.MyEventView.VIEW_NAME;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;

import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "manage-my-events")
@SideBarItem(sectionId = Sections.EVENT, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.FILE_PROCESS)
public class MyEventView extends AbstractView implements View, Serializable {

	public static final String VIEW_NAME = "Manage my events";

	private final MyEventPresenter presenter;

	@Autowired
	public MyEventView(MyEventPresenter presenter) {
		super(VIEW_NAME);
		this.presenter = presenter;
	}

	@Override
	public void initBody() {

	}
}
