package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ImageRenderer;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.util.ResourceUtil;
import hu.szeged.sporteventapp.ui.views.INotifier;

@SpringComponent
@ViewScope
public class ParticipantForm extends VerticalLayout implements INotifier {

	protected static final String H_LAYOUT_STYLE = "h-layout-with-top-bottom-padding";

	protected final ParticipantFormPresenter presenter;
	protected final EmailSendForm emailSendForm;

	protected TextField userNameFilter;
	protected TextField realNameFilter;
	protected Grid<User> grid;

	@Autowired
	public ParticipantForm(@Qualifier("participantFormPresenter") ParticipantFormPresenter presenter,
			EmailSendForm emailSendForm) {
		this.presenter = presenter;
		this.emailSendForm = emailSendForm;
		setSizeFull();
		initComponent();
	}

	protected void initComponent() {
		userNameFilter = new TextField();
		userNameFilter.setPlaceholder(USERNAME);
		realNameFilter = new TextField();
		realNameFilter.setPlaceholder(REAL_NAME);
		grid = buildGrid();
	}

	@PostConstruct
	private void init() {
		presenter.setParticipantForm(this);
	}

	private Grid<User> buildGrid() {
		final Grid<User> grid = new Grid<>(User.class);
		grid.setSizeFull();
		adjustGridCoumns(grid);

		return grid;
	}

	private void adjustGridCoumns(final Grid<User> grid) {
		grid.setColumns("username", "realName", "email");
		grid.addColumn(user -> ResourceUtil.setUserImageResource(user.getPictureName()), new ImageRenderer())
				.setStyleGenerator(o -> "grid-profile-picture").setCaption("Profile picture");
		grid.addComponentColumn(user -> {
			Button mailButton = new Button(VaadinIcons.ENVELOPE);
			mailButton.addClickListener(c -> {
				openEmailForm(grid.asSingleSelect().getOptionalValue());
			});
			return mailButton;
		}).setStyleGenerator(e -> "v-align-center").setCaption("Send mail");
	}

	private void openEmailForm(Optional<User> user) {
		if (user.isPresent()) {
			emailSendForm.setUser(user.get());
			emailSendForm.setVisible(true);
		} else {
			showInfoNotification(PLEASE_SELECT_A_ROW);
		}
	}

	public void constructParticipantForm(Optional<Set<User>> participants) {
		removeAllComponents();
		addComponentsAndExpand(new MCssLayout().withFullWidth()
				.add(new MHorizontalLayout().add(userNameFilter, realNameFilter), new MHorizontalLayout()
						.withStyleName(H_LAYOUT_STYLE).add(grid, emailSendForm).withFullSize().withExpand(grid, 1)));
		emailSendForm.setVisible(false);
		participants.ifPresent(p -> setParticipants(p));
	}

	public void setParticipants(Set<User> participants) {
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
}
