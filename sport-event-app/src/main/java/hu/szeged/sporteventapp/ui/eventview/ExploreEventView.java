package hu.szeged.sporteventapp.ui.eventview;

import static hu.szeged.sporteventapp.ui.eventview.ExploreEventView.VIEW_NAME;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.DateRenderer;

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
		grid = new Grid(SportEvent.class);

		grid.setHeight(100, Unit.PERCENTAGE);
		grid.setItems(generateDataForTest());
		grid.setColumns("name", "location", "organizerName");
		grid.addColumn(SportEvent::getStartDate,
				new DateRenderer("%1$tB %1$te, %1$tY", Locale.ENGLISH))
				.setCaption("startDate");
		grid.addColumn(SportEvent::getEndDate,
				new DateRenderer("%1$tB %1$te, %1$tY", Locale.ENGLISH))
				.setCaption("endDate");
		grid.addColumn(sportEvent -> "Participants",
				new ButtonRenderer(
						rendererClickEvent -> Notification.show("Participants")))
				.setCaption("Participants");
		grid.addColumn(sportEvent -> "Map",
				new ButtonRenderer(rendererClickEvent -> Notification.show("Map")))
				.setCaption("Map");
		grid.addColumn(sportEvent -> "Do you like join",
				new ButtonRenderer(
						rendererClickEvent -> Notification.show("Do you like join")))
				.setCaption("Do you like join?");
		addHeaderRowToGrid();
		grid.setSizeFull();
		addComponent(grid);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

	}

	private void addHeaderRowToGrid() {
		HeaderRow filterRow = grid.appendHeaderRow();
		createTextFieldFilter(filterRow, "name");
		createTextFieldFilter2(filterRow, "location");
		createTextFieldFilter(filterRow, "organizerName");
//		createDatePickerFilter(filterRow,"startDate");
//		createDatePickerFilter(filterRow,"endDate");
	}

	private void createTextFieldFilter(HeaderRow row, String columnName){
		HeaderCell cell = row.getCell(columnName);
		TextField filter = new TextField();
		filter.addValueChangeListener(this::onNameFilterTextChange);
		cell.setComponent(filter);
	}

	private void createTextFieldFilter2(HeaderRow row, String columnName){
		HeaderCell cell = row.getCell(columnName);
		TextField filter = new TextField();
		filter.addValueChangeListener(this::onNameFilterTextChange2);
		cell.setComponent(filter);
	}

	private void onNameFilterTextChange2(HasValue.ValueChangeEvent<String> event) {
		ListDataProvider<SportEvent> dataProvider = (ListDataProvider<SportEvent>) grid.getDataProvider();
		dataProvider.setFilter(SportEvent::getLocation, s -> caseInsensitiveContains(s, event.getValue()));
	}


	private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
		ListDataProvider<SportEvent> dataProvider = (ListDataProvider<SportEvent>) grid.getDataProvider();
		dataProvider.setFilter(SportEvent::getName, s -> caseInsensitiveContains(s, event.getValue()));
	}

	private Boolean caseInsensitiveContains(String where, String what) {
		return where.toLowerCase().contains(what.toLowerCase());
	}

	private void createDatePickerFilter(HeaderRow row, String columnName) {
		HeaderCell cell = row.getCell(columnName);
		DateField filter = new DateField();
		filter.addValueChangeListener(valueChangeEvent -> System.out.println(valueChangeEvent.getValue()));
		cell.setComponent(filter);
	}

	private List<SportEvent> generateDataForTest() {
		ArrayList<SportEvent> tests = new ArrayList();
		tests.add(new SportEvent("1", "a", new Date(), new Date(), "asdd", "a1"));
		tests.add(new SportEvent("12", "d", new Date(), new Date(), "afsd", "a1"));
		tests.add(new SportEvent("13", "adff", new Date(), new Date(), "asgasd", "a1"));
		tests.add(new SportEvent("14", "a", new Date(), new Date(), "assadfd", "a1"));
		tests.add(new SportEvent("15", "gsdfa", new Date(), new Date(), "aaqsd", "a1"));
		tests.add(new SportEvent("16", "a", new Date(), new Date(), "asewqd", "a1"));
		tests.add(new SportEvent("17", "a", new Date(), new Date(), "asztrd", "a1"));
		tests.add(new SportEvent("18", "aasd", new Date(), new Date(), "asuzd", "a1"));
		tests.add(new SportEvent("18", "adds", new Date(), new Date(), "asuzd", "a1"));
		tests.add(new SportEvent("18", "a", new Date(), new Date(), "asuzd", "a1"));
		tests.add(new SportEvent("18", "gh", new Date(), new Date(), "asuzd", "a1"));
		tests.add(new SportEvent("18", "aghgt", new Date(), new Date(), "asuzd", "a1"));
		tests.add(new SportEvent("18", "a", new Date(), new Date(), "asuzd", "a1"));
		return tests;
	}
}
