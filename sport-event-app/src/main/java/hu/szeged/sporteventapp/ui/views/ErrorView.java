package hu.szeged.sporteventapp.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ErrorView extends VerticalLayout implements View {

    private Label message;

    public ErrorView() {
        setMargin(true);
        addComponent(message = new Label());
        message.setSizeUndefined();
        message.addStyleName(ValoTheme.LABEL_FAILURE);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        message.setValue(String.format("No such view: %s", event.getViewName()));
    }
}
