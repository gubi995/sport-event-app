package hu.szeged.sporteventapp.operations;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import hu.szeged.sporteventapp.ui.Sections;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.managed.VaadinManagedSecurity;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Logout")
@VaadinFontIcon(VaadinIcons.POWER_OFF)
public class LogoutOperation implements Runnable {

    private final VaadinManagedSecurity vaadinSecurity;

    @Autowired
    public LogoutOperation(VaadinManagedSecurity vaadinSecurity) {
        this.vaadinSecurity = vaadinSecurity;
    }

    @Override
    public void run() {
        vaadinSecurity.logout("?logout");
    }
}
