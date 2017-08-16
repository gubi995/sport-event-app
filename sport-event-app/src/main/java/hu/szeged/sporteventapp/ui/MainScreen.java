/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hu.szeged.sporteventapp.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import org.vaadin.spring.sidebar.security.VaadinSecurityItemFilter;

import com.vaadin.navigator.Navigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import hu.szeged.sporteventapp.ui.views.AccessDeniedView;
import hu.szeged.sporteventapp.ui.views.ErrorView;

/**
 * Full-screen UI component that allows the user to navigate between views, and log out.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@PrototypeScope
@SpringComponent
public class MainScreen extends CustomComponent {

	private final VaadinSecurity vaadinSecurity;
	private final SpringViewProvider springViewProvider;
	private final ValoSideBar sideBar;
	private CssLayout viewContainer;

	@Autowired
	public MainScreen(VaadinSecurity vaadinSecurity,
			SpringViewProvider springViewProvider, ValoSideBar sideBar) {
		this.vaadinSecurity = vaadinSecurity;
		this.springViewProvider = springViewProvider;
		this.sideBar = sideBar;
		initLayout();
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
		layout.addComponent(viewContainer);
		layout.setExpandRatio(viewContainer, 1f);
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

}
