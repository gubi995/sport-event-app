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
package hu.szeged.sporteventapp.operations;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.backend.MyBackend;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 * An operation that invokes a backend method that is available for all users.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "User operation", order = 0)
@FontAwesomeIcon(FontAwesome.ANCHOR)
public class UserOperation implements Runnable {

    private final MyBackend backend;

    @Autowired
    public UserOperation(MyBackend backend) {
        this.backend = backend;
    }

    @Override
    public void run() {
        Notification.show(backend.echo("Hello World"));
    }
}
