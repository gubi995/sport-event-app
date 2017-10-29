package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.DELETE;
import static hu.szeged.sporteventapp.ui.constants.ViewConstants.PLEASE_SELECT_A_ROW;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.User;

@UIScope
@SpringComponent
public class ParticipantFormAdminMode extends ParticipantForm {

	private Button deleteButton;

	@Autowired
	public ParticipantFormAdminMode(ParticipantFormPresenter presenter, EmailSendForm emailSendForm) {
		super(presenter, emailSendForm);
	}

	@Override
	protected void initComponent() {
		super.initComponent();
		deleteButton = new Button(DELETE);
		deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);
		deleteButton.addClickListener(e -> {
			delete(grid.asSingleSelect().getOptionalValue());
		});
	}

	@Override
	public void constructParticipantForm(Optional<Set<User>> participants) {
		removeAllComponents();
		addComponentsAndExpand(
				new MCssLayout()
						.withFullWidth().add(
								new MHorizontalLayout().withSpacing(true).withFullWidth()
										.add(new MHorizontalLayout().add(userNameFilter, realNameFilter),
												Alignment.MIDDLE_LEFT)
										.add(new MHorizontalLayout().add(deleteButton), Alignment.MIDDLE_RIGHT),
								new MHorizontalLayout().withStyleName(H_LAYOUT_STYLE).add(grid, emailSendForm)
										.withFullSize().withExpand(grid, 1)));
		emailSendForm.setVisible(false);
		participants.ifPresent(p -> setParticipants(p));
	}

	private void delete(Optional<User> user) {
		if (user.isPresent()) {
			presenter.delete(user.get());
			presenter.updateGridData();
		} else {
			showInfoNotification(PLEASE_SELECT_A_ROW);
		}
	}
}
