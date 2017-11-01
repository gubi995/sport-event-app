package hu.szeged.sporteventapp.ui.views.home;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.data.enums.Role;
import hu.szeged.sporteventapp.backend.data.enums.SportType;
import hu.szeged.sporteventapp.backend.service.SportEventService;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;

@UIScope
@SpringComponent
public class HomePresenter extends AbstractPresenter<HomeView> {

	private final SportEventService sportEventService;

	@Autowired
	public HomePresenter(UserService userService, SportEventService sportEventService) {
		super(userService);
		this.sportEventService = sportEventService;
	}

	private Map<String, Integer> gatherSportByPopularity() {
		Map<String, Integer> map = new LinkedHashMap<>();
		for (String s : SportType.getAllSportType()) {
			map.put(s, 0);
			for (SportEvent e : sportEventService.findSportEventBySportType(s)) {
				int number = map.get(s);
				map.put(s, number + e.getParticipants().size());
			}
		}
		return map.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));
	}

	private Map<User, Integer> gatherMostSportyMembers() {
		Map<User, Integer> map = new HashMap<>();
		for (User user : userService.findAll()) {
			map.put(user, user.getEventsImAttending().size());
		}

		return map.entrySet().stream().sorted(Map.Entry.<User, Integer> comparingByValue().reversed()).limit(5)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));
	}

	private int countPopulationOfPage() {
		return userService.countAllByRole(Role.USER);
	}

	@Override
	public void enter() {
		super.enter();
		gatherSportByPopularity();
		getView().getMemberCounter().setPopulationNumber(String.valueOf(countPopulationOfPage()));
		getView().getMostSportyMembers().generateContent(gatherMostSportyMembers());
	}
}
