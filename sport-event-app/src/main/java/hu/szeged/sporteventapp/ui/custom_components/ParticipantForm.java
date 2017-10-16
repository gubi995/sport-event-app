package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.util.ImageUtil;
import hu.szeged.sporteventapp.ui.views.INotifier;

@ViewScope
@SpringComponent
public class ParticipantForm extends VerticalLayout implements INotifier {

	private static final String H_LAYOUT_STYLE = "h-layout-with-top-bottom-padding";

	private final ParticipantFormPresenter presenter;

	private EmailSendForm emailSendForm;
	private TextField userNameFilter;
	private TextField realNameFilter;
	private Button deleteButton;
	private Grid<User> grid;

	@Autowired
	public ParticipantForm(ParticipantFormPresenter presenter) {
		this.presenter = presenter;
		setSizeFull();
		initComponent();
	}

	private void initComponent() {
		emailSendForm = new EmailSendForm();
		userNameFilter = new TextField();
		userNameFilter.setPlaceholder(USERNAME);
		realNameFilter = new TextField();
		realNameFilter.setPlaceholder(REAL_NAME);
		deleteButton = new Button(DELETE);
		deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);
		deleteButton.addClickListener(e -> {
			presenter.delete(grid.asSingleSelect().getValue());
			presenter.updateGridData();
		});
		grid = buildGrid();
	}

	private Grid<User> buildGrid() {
		final Grid<User> grid = new Grid<>(User.class);
		grid.setSizeFull();

		adjustGridCoumns(grid);

		return grid;
	}

	private void adjustGridCoumns(final Grid<User> grid) {
		grid.setColumns("username", "realName", "email");
		grid.addColumn(user -> ImageUtil.setImageThemeResource(user.getPictureName()), new ImageRenderer())
				.setStyleGenerator(o -> "grid-profile-picture").setCaption("Profile picture");
		grid.addComponentColumn(user -> {
			Button mailButton = new Button(VaadinIcons.ENVELOPE);
			mailButton.addClickListener(c -> {
				emailSendForm.setVisible(true);
			});
			return mailButton;
		}).setStyleGenerator(e -> "v-align-center").setCaption("Send mail");
	}

	public void constructParticipantForm(Optional<Set<User>> participants) {
		removeAllComponents();
		addComponentsAndExpand(new MCssLayout().withFullWidth()
				.add(new MHorizontalLayout().add(userNameFilter, realNameFilter), new MHorizontalLayout()
						.withStyleName(H_LAYOUT_STYLE).add(grid, emailSendForm).withFullSize().withExpand(grid, 1)));
		emailSendForm.setVisible(false);
		participants.ifPresent(p -> setParticipants(p));
	}

	public void constructParticipantFormInAdminMode(Optional<Set<User>> participants) {
		removeAllComponents();
		addComponentsAndExpand(
				new MVerticalLayout()
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

	private void setParticipants(Set<User> participants) {
		grid.setItems(participants);
		initFilters();
	}

	private void initFilters() {
		ListDataProvider<User> dataProvider = (ListDataProvider<User>) grid.getDataProvider();
		bindFiltersAndGrid(dataProvider, userNameFilter, realNameFilter);
	}

	private void bindFiltersAndGrid(final ListDataProvider<User> dataProvider, AbstractField... fields) {
		Arrays.stream(fields).forEach(field -> field.addValueChangeListener(e -> updateFilters(dataProvider)));
	}

	private void updateFilters(final ListDataProvider<User> dataProvider) {
		dataProvider.clearFilters();
		dataProvider.addFilter(user -> caseInsensitiveContains(user.getUsername(), userNameFilter.getValue()));
		dataProvider.addFilter(user -> caseInsensitiveContains(user.getRealName(), realNameFilter.getValue()));
	}

	private Boolean caseInsensitiveContains(String where, String what) {
		return where.toLowerCase().contains(what.toLowerCase());
	}

	public void showInWindow(UI ui) {
		Window window = new Window(PARTICIPANTS);
		window.setSizeFull();
		// window.setWidth(800, Sizeable.Unit.PIXELS);
		// window.setHeight(600, Sizeable.Unit.PIXELS);
		window.setIcon(VaadinIcons.GROUP);
		window.setResizable(false);
		window.setDraggable(false);
		window.setContent(this);
		window.center();
		ui.addWindow(window);
	}

	private class EmailSendForm extends VerticalLayout {

		private static final String CAPTION_STYLE = "email-label";

		TextField subjectField;
		Label toUsernameLabel;
		Label toEmailLabel;
		TextArea textArea;
		Button sendButton;
		Button hideButton;

		public EmailSendForm() {
			super();
			setMargin(false);
			setSizeUndefined();
			initComponent();
			addComponents(new MLabel("Send mail").withStyleName(ValoTheme.LABEL_H2, CAPTION_STYLE).withFullWidth(),
					subjectField, toUsernameLabel, toEmailLabel, textArea,
					new HorizontalLayout(sendButton, hideButton));
		}

		private void initComponent() {
			subjectField = new TextField(SUBJECT);
			toUsernameLabel = new Label();
			toUsernameLabel.setCaption("Addressee username:");
			toEmailLabel = new Label();
			toEmailLabel.setCaption("Addressee email: ");
			textArea = new TextArea(MESSAGE);
			textArea.setHeight(350, Unit.PIXELS);
			textArea.setWidth(500, Unit.PIXELS);
			sendButton = new Button(SEND);
			sendButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			sendButton.addClickListener(c -> send());
			hideButton = new Button(HIDE);
			hideButton.addClickListener(c -> this.setVisible(false));
		}

		private void setAddressee(User user) {
			setFromData(user);
		}

		private void setFromData(User user) {
			toUsernameLabel.setValue(user.getUsername());
			toEmailLabel.setValue(user.getEmail());
		}

		private void send() {
			// if (binder.isValid()) {
			// presenter.send();
			// } else {
			// showWarningNotification(VALIDATION_WARNING_MSG);
			// }
		}
	}
}
