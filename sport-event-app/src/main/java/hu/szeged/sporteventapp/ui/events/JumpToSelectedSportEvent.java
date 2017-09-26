package hu.szeged.sporteventapp.ui.events;

import org.springframework.context.ApplicationEvent;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;

public class JumpToSelectedSportEvent extends ApplicationEvent {

	private final SportEvent sportEvent;

	public JumpToSelectedSportEvent(Object source, SportEvent sportEvent) {
		super(source);
		this.sportEvent = sportEvent;
	}

	public Object getSource() {
		return super.getSource();
	}

	public SportEvent getSportEvent() {
		return sportEvent;
	}
}
