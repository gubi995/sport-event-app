package hu.szeged.sporteventapp.ui.mainscreen;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import org.vaadin.spring.sidebar.security.VaadinSecurityItemFilter;

import com.vaadin.navigator.Navigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import hu.szeged.sporteventapp.ui.views.AccessDeniedView;
import hu.szeged.sporteventapp.ui.views.ErrorView;

@UIScope
@SpringComponent
public class MainScreen extends CustomComponent {

	private static final String STYLE_NAME = "view-container";

	private final VaadinSecurity vaadinSecurity;
	private final SpringViewProvider springViewProvider;
	private final ValoSideBar sideBar;
	private CssLayout viewContainer;

	@Autowired
	public MainScreen(VaadinSecurity vaadinSecurity, SpringViewProvider springViewProvider, ValoSideBar sideBar) {
		this.vaadinSecurity = vaadinSecurity;
		this.springViewProvider = springViewProvider;
		this.sideBar = sideBar;
		initLayout();
	}

	public void initNavigator() {
		Navigator navigator = new Navigator(UI.getCurrent(), viewContainer);
		// Without an AccessDeniedView, the view provider would act like the restricted
		// views did not exist at all.
		springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		navigator.addProvider(springViewProvider);
		navigator.setErrorView(ErrorView.class);
		navigator.navigateTo(navigator.getState());
	}

	private void initLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		setSizeFull();
		setCompositionRoot(layout);

		// By adding a security item filter, only views that are accessible to the user
		// will show up in the side bar.
		sideBar.setItemFilter(new VaadinSecurityItemFilter(vaadinSecurity));
		layout.addComponent(sideBar);

		viewContainer = new CssLayout();
		viewContainer.setSizeFull();
		viewContainer.addStyleName(STYLE_NAME);
		layout.addComponent(viewContainer);
		layout.setExpandRatio(viewContainer, 1f);
	}
}
