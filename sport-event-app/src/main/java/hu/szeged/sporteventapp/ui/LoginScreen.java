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

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

import java.io.File;

/**
 * Full-screen UI component that allows the user to login.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@PrototypeScope
@SpringComponent
public class LoginScreen extends CustomComponent {

    private final VaadinSecurity vaadinSecurity;
    private final EventBus.SessionEventBus eventBus;

    private TextField userNameFieldForLogin;
    private TextField userNameFieldForSingUp;
    private TextField emailFieldForSingUp;
    private PasswordField passwordFieldForLogin;
    private PasswordField passwordFieldForSingUp;
    private PasswordField passwordFieldForValidate;
    private Button loginButton;
    private Button singUpButton;
    private Label loginFailedLabel;
    private Label loggedOutLabel;
    private Label singUpStateLabel;
    private Image backgroundImage;

    @Autowired
    public LoginScreen(VaadinSecurity vaadinSecurity, EventBus.SessionEventBus eventBus) {
        this.vaadinSecurity = vaadinSecurity;
        this.eventBus = eventBus;
        initLayout();
    }

    void setLoggedOut(boolean loggedOut) {
//        loggedOutLabel.setVisible(loggedOut);
    }

    private void initLayout() {
        HorizontalLayout root = new HorizontalLayout();
        VerticalLayout rightLayout = new VerticalLayout();
        CssLayout leftLayout = new CssLayout();

        FileResource resource = new FileResource(new File("/home/gruber/Gubi/Programming/Java/Vaadin/sport-event-app/src/main/resources/images/background.jpg"));

        backgroundImage = new Image();
        backgroundImage.setSource(resource);
        leftLayout.addComponent(backgroundImage);

        Panel loginPanel = new Panel();
        FormLayout loginForm = new FormLayout();
        loginForm.setMargin(true);
        loginForm.setSpacing(true);
        userNameFieldForLogin = new TextField("Username");
        passwordFieldForLogin = new PasswordField("Password");
        loginButton = new Button("Login");
        Label loginLabel = new Label("Login");
        loginLabel.setStyleName(ValoTheme.LABEL_H3);
        loginForm.addComponents(loginLabel, userNameFieldForLogin, passwordFieldForLogin, loginButton);
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setDisableOnClick(true);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addClickListener((Button.ClickListener) event -> login());
        loginPanel.setContent(loginForm);
        rightLayout.addComponent(loginPanel);

        Panel singUpPanel = new Panel();
        FormLayout singUpForm = new FormLayout();
        singUpForm.setMargin(true);
        singUpForm.setSpacing(true);
        userNameFieldForSingUp = new TextField("Username");
        emailFieldForSingUp = new TextField("Email");
        passwordFieldForSingUp = new PasswordField("Password");
        passwordFieldForValidate = new PasswordField("Password again");
        singUpButton = new Button("Sing up");
        Label singUpLabel = new Label("Sing up");
        singUpLabel.setStyleName(ValoTheme.LABEL_H3);
        singUpForm.addComponents(singUpLabel, userNameFieldForSingUp, emailFieldForSingUp,
                passwordFieldForSingUp, passwordFieldForValidate, singUpButton);
        singUpPanel.setContent(singUpForm);
        rightLayout.addComponent(singUpPanel);


//        VerticalLayout loginLayout = new VerticalLayout();
//        loginLayout.setSizeUndefined();
//
//        loginFailedLabel = new Label();
//        loginLayout.addComponent(loginFailedLabel);
//        loginLayout.setComponentAlignment(loginFailedLabel, Alignment.BOTTOM_CENTER);
//        loginFailedLabel.setSizeUndefined();
//        loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
//        loginFailedLabel.setVisible(false);
//
//        loggedOutLabel = new Label("Good bye!");
//        loginLayout.addComponent(loggedOutLabel);
//        loginLayout.setComponentAlignment(loggedOutLabel, Alignment.BOTTOM_CENTER);
//        loggedOutLabel.setSizeUndefined();
//        loggedOutLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
//        loggedOutLabel.setVisible(false);
//
//        loginLayout.addComponent(loginForm);
//        loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

        root.setSizeFull();
        root.addComponents(leftLayout, rightLayout);
        setCompositionRoot(root);
        setSizeFull();
//        VerticalLayout rootLayout = new VerticalLayout(loginLayout);
//        rootLayout.setSizeFull();
//        rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
//        setCompositionRoot(rootLayout);
//        setSizeFull();
    }

    private void login() {
        try {
            loggedOutLabel.setVisible(false);

            String password = passwordFieldForLogin.getValue();
            passwordFieldForLogin.setValue("");

            final Authentication authentication = vaadinSecurity.login(userNameFieldForLogin.getValue(), password);
            eventBus.publish(this, new SuccessfulLoginEvent(getUI(), authentication));
        } catch (AuthenticationException ex) {
            userNameFieldForLogin.focus();
            userNameFieldForLogin.selectAll();
            loginFailedLabel.setValue(String.format("Login failed: %s", ex.getMessage()));
            loginFailedLabel.setVisible(true);
        } catch (Exception ex) {
            Notification.show("An unexpected error occurred", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            LoggerFactory.getLogger(getClass()).error("Unexpected error while logging in", ex);
        } finally {
            loginButton.setEnabled(true);
        }
    }

    private void register() {

    }
}
