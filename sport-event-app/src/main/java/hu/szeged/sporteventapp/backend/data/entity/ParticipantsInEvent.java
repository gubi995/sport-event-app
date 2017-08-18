package hu.szeged.sporteventapp.backend.data.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

public class ParticipantsInEvent extends AbstractEntity {

	@NotNull
	SportEvent sportEvent;

	@NotNull
	List<User> participants;

	public ParticipantsInEvent(SportEvent sportEvent, List<User> participants) {
		this.sportEvent = sportEvent;
		this.participants = participants;
	}

	public SportEvent getSportEvent() {
		return sportEvent;
	}

	public void setSportEvent(SportEvent sportEvent) {
		this.sportEvent = sportEvent;
	}

	public List<User> getParticipants() {
		return participants;
	}

	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}
}
