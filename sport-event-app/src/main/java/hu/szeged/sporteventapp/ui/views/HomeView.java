package hu.szeged.sporteventapp.ui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.ui.Sections;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

@SpringView(name = "")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Home", order = 0)
@VaadinFontIcon(VaadinIcons.HOME)
@ViewScope
public class HomeView extends VerticalLayout implements View {

	public HomeView() {
		setSpacing(true);
		setMargin(true);

		Label header = new Label("Welcome to the Vaadin Managed Security Demo!");
		header.addStyleName(ValoTheme.LABEL_H1);
		addComponent(header);

		Label body = new Label(
				"<p>This application demonstrate how a Vaadin application can take care of security itself while still integrating with Spring Security.</p>"
						+ "<p>Please try it out by clicking and navigating around as different users. You can log in as <em>user/user</em> or <em>admin/admin</em>. Some of the protected "
						+ "features are hidden from the UI when you cannot access them, others are visible all the time.</p>"
						+ "<p>Also note that since we are using web socket based push, we do not have access to cookies and therefore cannot use Remember Me services.</p>");
		body.setContentMode(ContentMode.HTML);
		addComponent(body);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}
}
