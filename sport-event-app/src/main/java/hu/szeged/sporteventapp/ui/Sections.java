package hu.szeged.sporteventapp.ui;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

@Component
@SideBarSections({ @SideBarSection(id = Sections.VIEWS, caption = "Views"),
		@SideBarSection(id = Sections.EVENT, caption = "Event"),
		@SideBarSection(id = Sections.PROFILE, caption = "Profile"),
		@SideBarSection(id = Sections.ADMIN, caption = "Admin"),
		@SideBarSection(id = Sections.OPERATIONS, caption = "Operations") })
public class Sections {

	public static final String VIEWS = "views";
	public static final String OPERATIONS = "operations";
	public static final String EVENT = "event";
	public static final String PROFILE = "profile";
	public static final String ADMIN = "admin";
}
