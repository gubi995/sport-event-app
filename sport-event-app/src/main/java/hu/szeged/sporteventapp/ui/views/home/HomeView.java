package hu.szeged.sporteventapp.ui.views.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.data.enums.SportType;
import hu.szeged.sporteventapp.ui.Sections;
import hu.szeged.sporteventapp.ui.views.AbstractView;

@SpringView(name = "")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Home", order = 0)
@VaadinFontIcon(VaadinIcons.HOME)
@ViewScope
public class HomeView extends AbstractView {

	public static final String VIEW_NAME = "Welcome to the sport community app";

	private final HomePresenter presenter;

	private IntroductionPanel introductionPanel;
	private MemberCounter memberCounter;
	private MostSportyMembers mostSportyMembers;
	private SinglePieChartView singlePieChartView;

	@Autowired
	public HomeView(HomePresenter presenter) {
		super(VIEW_NAME);
		this.presenter = presenter;
	}

	@Override
	public void initComponent() {
		introductionPanel = new IntroductionPanel();
		memberCounter = new MemberCounter();
		mostSportyMembers = new MostSportyMembers();
		singlePieChartView = new SinglePieChartView();
	}

	@Override
	public void initBody() {
		memberCounter.setPopulationNumber("546");
		MHorizontalLayout topLayout = new MHorizontalLayout().add(introductionPanel, memberCounter);
		MHorizontalLayout bottomLayout = new MHorizontalLayout().add(singlePieChartView, mostSportyMembers);
		addComponentsAndExpand(new MVerticalLayout().withMargin(false).add(topLayout, bottomLayout)
				.withExpandRatio(topLayout, 0.2f).withExpandRatio(bottomLayout, 0.8f));
	}

	@PostConstruct
	private void initPresenter() {
		presenter.setView(this);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		presenter.enter();
	}

	public MemberCounter getMemberCounter() {
		return memberCounter;
	}

	public MostSportyMembers getMostSportyMembers() {
		return mostSportyMembers;
	}

	public SinglePieChartView getSinglePieChartView() {
		return singlePieChartView;
	}

	public class MemberCounter extends Panel {

		private Label populationNumber;

		public MemberCounter() {
			populationNumber = new Label();
			setContent(new MVerticalLayout().add(new MLabel("Population of the page").withStyleName(ValoTheme.LABEL_H3),
					populationNumber));
		}

		public void setPopulationNumber(String populationNumber) {
			this.populationNumber.setValue(populationNumber);
		}
	}

	public class MostSportyMembers extends Panel {

		MVerticalLayout layout;

		public MostSportyMembers() {
			layout = new MVerticalLayout();
			layout.add(new MLabel("Most sporty members(Username - Real name - Number of activity):")
					.withStyleName(ValoTheme.LABEL_H3));
			setContent(layout);
		}

		public void generateContent(Map<User, Integer> members) {
			members.forEach((user, integer) -> {
				layout.addComponent(new MLabel(user.getUsername() + " - " + user.getRealName() + " - " + integer));
			});
		}
	}

	public class IntroductionPanel extends Panel {

		private static final String WELCOME_TEXT = "I hope this website will help you to find sports possibilities"
				+ " and make new friendship. The other waiting for you that join their sports events or "
				+ "organize one!";

		public IntroductionPanel() {
			setContent(new MVerticalLayout().add(new MLabel("Hello here!").withStyleName(ValoTheme.LABEL_H3),
					new MLabel(WELCOME_TEXT)));
		}
	}

	public class SinglePieChartView extends Panel {

		public SinglePieChartView() {
			setWidth(750, Unit.PIXELS);
		}

		public void generateChart(Map<String, Integer> sports) {
			Component chart = getChart(sports);
			chart.setWidth(600, Unit.PIXELS);
			setContent(new MVerticalLayout().add(chart).withAlign(chart, Alignment.MIDDLE_CENTER));
		}

		private Component getChart(Map<String, Integer> sports) {
			PieChartConfig config = new PieChartConfig();
			config.data().labels(SportType.getAllSportType().toArray(new String[0]))
					.addDataset(new PieDataset().label("Dataset")).and();

			config.options().responsive(true).title().display(true).text("Sports popularity").and().animation()
					// .animateScale(true)
					.animateRotate(true).and().done();

			List<String> labels = config.data().getLabels();
			for (Dataset<?, ?> ds : config.data().getDatasets()) {
				PieDataset lds = (PieDataset) ds;
				List<Double> data = new ArrayList<>();
				List<String> colors = new ArrayList<>();
				for (String label : labels) {
					data.add((double) sports.get(label));
					colors.add(ColorUtils.randomColor(0.7));
				}
				lds.backgroundColor(colors.toArray(new String[colors.size()]));
				lds.dataAsList(data);
			}

			ChartJs chart = new ChartJs(config);
			chart.setJsLoggingEnabled(true);

			return chart;
		}

	}
}
