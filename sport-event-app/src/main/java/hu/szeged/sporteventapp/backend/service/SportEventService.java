package hu.szeged.sporteventapp.backend.service;

import hu.szeged.sporteventapp.backend.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.repositories.SportEventRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    Set<User> findParticipantsOfSportEvent(String name){
        SportEvent sportEvent = eventRepository.findSportEventByName(name);
        if (sportEvent == null){
            return new HashSet<>();
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
}
