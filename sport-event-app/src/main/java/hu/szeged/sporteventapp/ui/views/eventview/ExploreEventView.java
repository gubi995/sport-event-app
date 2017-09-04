package hu.szeged.sporteventapp.ui.views.eventview;

import static hu.szeged.sporteventapp.ui.views.eventview.ExploreEventView.VIEW_NAME;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "explore-events")
@SideBarItem(sectionId = Sections.EVENT, caption = VIEW_NAME)
@VaadinFontIcon(VaadinIcons.FILE_SEARCH)
public class ExploreEventView extends AbstractView implements View, Serializable {

	public static final String VIEW_NAME = "Explore events";

	private final ExploreEventPresenter presenter;

	private Grid<SportEvent> grid;

	@Autowired
	public ExploreEventView(ExploreEventPresenter presenter) {
		super(VIEW_NAME);
		this.presenter = presenter;
	}

	@Override
	public void initBody() {
		grid = buildGrid();
		addComponentsAndExpand(grid);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

	}

	private Grid<SportEvent> buildGrid() {
		final Grid<SportEvent> grid = new Grid<>(SportEvent.class);
		grid.setSizeFull();

		List<SportEvent> items = generateDataForTest();
		grid.setItems(items);

		setColumnRenderes(grid);

		grid.setColumns("name", "location", "startDate", "endDate");

		initFilters(grid);

		return grid;
	}

	private void setColumnRenderes(final Grid grid) {
		grid.setColumns("name", "location", "startDate", "endDate");
		grid.getColumn("name").setRenderer(new TextRenderer());
		grid.getColumn("location").setRenderer(new TextRenderer());
		grid.getColumn("startDate").setWidth(250)
				.setRenderer(new DateRenderer("%1$tB %1$te, %1$tY", Locale.ENGLISH));
		grid.getColumn("endDate").setWidth(250)
				.setRenderer(new DateRenderer("%1$tB %1$te, %1$tY", Locale.ENGLISH));
	}

	private void initFilters(final Grid<SportEvent> grid) {

	}

	private List<SportEvent> generateDataForTest() {
		ArrayList<SportEvent> tests = new ArrayList();
		tests.add(new SportEvent("1", "a", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("12", "d", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("13", "adff", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("14", "a", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("15", "gsdfa", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("16", "a", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("17", "a", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("18", "aasd", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("18", "adds", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("18", "a", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("18", "gh", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("18", "aghgt", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		tests.add(new SportEvent("18", "a", LocalDateTime.now(), LocalDateTime.now(), 20, "a1"));
		return tests;
	}
}
