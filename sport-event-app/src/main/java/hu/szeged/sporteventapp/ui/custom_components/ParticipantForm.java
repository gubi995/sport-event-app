package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ImageRenderer;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.common.util.ImageUtil;
import hu.szeged.sporteventapp.ui.views.INotifier;

@ViewScope
@SpringComponent
public class ParticipantForm extends VerticalLayout implements INotifier {

	private TextField userNameFilter;
	private TextField realNameFilter;
	private Grid<User> grid;

	@Autowired
	public ParticipantForm() {
		setSizeFull();
		initComponent();
	}

	private void initComponent() {
		userNameFilter = new TextField();
		userNameFilter.setPlaceholder(USERNAME);
		realNameFilter = new TextField();
		realNameFilter.setPlaceholder(REAL_NAME);
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
		grid.addColumn(user -> ImageUtil.setImageThemeResource(user.getPictureName()),
				new ImageRenderer()).setStyleGenerator(o -> "grid-profile-picture")
				.setCaption("Profile picture");
		grid.addComponentColumn(user -> {
			Button button = new Button(VaadinIcons.ENVELOPE);
			button.addClickListener(c -> {
				Notification.show("Click");
			});
			return button;
		}).setStyleGenerator(e -> "v-align-center").setCaption("Send mail");
	}

	public void constructParticipantForm(Optional<List<User>> participants) {
		removeAllComponents();
		addComponents(new MHorizontalLayout(userNameFilter, realNameFilter));
		addComponentsAndExpand(grid);
		participants.ifPresent(p -> setParticipants(p));
	}

	private void setParticipants(List<User> participants) {
		grid.setItems(participants);
		initFilters();
	}

	private void initFilters() {
		ListDataProvider<User> dataProvider = (ListDataProvider<User>) grid
				.getDataProvider();
		bindFiltersAndGrid(dataProvider, userNameFilter, realNameFilter);
	}

	private void bindFiltersAndGrid(final ListDataProvider<User> dataProvider,
			AbstractField... fields) {
		Arrays.stream(fields).forEach(
				field -> field.addValueChangeListener(e -> updateFilters(dataProvider)));
	}

	private void updateFilters(final ListDataProvider<User> dataProvider) {
		dataProvider.clearFilters();
		dataProvider.addFilter(user -> caseInsensitiveContains(user.getUsername(),
				userNameFilter.getValue()));
		dataProvider.addFilter(user -> caseInsensitiveContains(user.getRealName(),
				realNameFilter.getValue()));
	}

	private Boolean caseInsensitiveContains(String where, String what) {
		return where.toLowerCase().contains(what.toLowerCase());
	}

	public void showInWindow(UI ui) {
		Window window = new Window(PARTICIPANTS);
		// window.setSizeFull();
		window.setWidth(800, Sizeable.Unit.PIXELS);
		window.setHeight(600, Sizeable.Unit.PIXELS);
		window.setIcon(VaadinIcons.GROUP);
		window.setResizable(false);
		window.setDraggable(false);
		window.setContent(this);
		window.center();
		ui.addWindow(window);
	}
}
