package hu.szeged.sporteventapp.common.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.ui.views.calenarview.SportEventItem;

@Component
public class SportEventConverter {

	public List<SportEventItem> convertSportEventsToSportEventItems(List<SportEvent> sportEvents) {
		return sportEvents.stream().map(sportEvent -> new SportEventItem(sportEvent)).collect(Collectors.toList());
	}
}
