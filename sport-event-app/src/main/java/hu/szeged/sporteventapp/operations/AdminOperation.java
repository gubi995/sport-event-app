package hu.szeged.sporteventapp.operations;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.backend.MyBackend;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Admin operation", order = 1)
@FontAwesomeIcon(FontAwesome.WRENCH)
public class AdminOperation implements Runnable {

    private final MyBackend backend;

    @Autowired
    public AdminOperation(MyBackend backend) {
        this.backend = backend;
    }

    @Override
    public void run() {
        Notification.show(backend.adminOnlyEcho("Hello Admin World"));
    }
}
