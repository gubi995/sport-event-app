package hu.szeged.sporteventapp.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.repositories.SportEventRepository;
import hu.szeged.sporteventapp.common.exception.AlreadyJoinedException;
import hu.szeged.sporteventapp.common.exception.NoEmptyPlaceException;
import hu.szeged.sporteventapp.common.exception.NotParticipantException;

@Service
public class SportEventService {

	SportEventRepository eventRepository;

	@Autowired
	public SportEventService(SportEventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

    public List<SportEvent> findAll(){
	    return eventRepository.findAll();
    }

    public List<SportEvent> findSportEventByOrganizer(User user){
        return eventRepository.findSportEventByOrganizer(user);
    }

    SportEvent findSportEventByName(String name){
        return eventRepository.findSportEventByName(name);
    }

	List<User> findParticipantsOfSportEvent(String name) {
        SportEvent sportEvent = eventRepository.findSportEventByName(name);
        if (sportEvent == null){
			return new ArrayList<>();
        }else {
            return sportEvent.getParticipants();
        }
    }

	@Transactional
	public void delete(String sportEventId) {
		eventRepository.deleteById(sportEventId);
	}

	@Transactional
	public SportEvent save(SportEvent sportEvent) {
		return eventRepository.save(sportEvent);
	}

	@Transactional
	public void joinToSportEvent(SportEvent sportEvent, User user)
			throws AlreadyJoinedException, NoEmptyPlaceException {
		if (sportEvent.getParticipants().size() <= sportEvent.getMaxParticipant()) {
			if (!sportEvent.getParticipants().contains(user)) {
				sportEvent.getParticipants().add(user);
				eventRepository.save(sportEvent);
			}
			else {
				throw new AlreadyJoinedException("It seems you are already joined");
			}
		}
		else {
			throw new NoEmptyPlaceException("Sorry, there is no empty place :(");
		}
	}

	@Transactional
	public void leaveFromSportEvent(SportEvent sportEvent, User user)
			throws NotParticipantException {
		if (sportEvent.getParticipants().contains(user)) {
			sportEvent.getParticipants().remove(user);
			eventRepository.save(sportEvent);
		}
		else {
			throw new NotParticipantException("You are not participate on this event");
		}
	}
}
