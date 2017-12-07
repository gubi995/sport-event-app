package hu.szeged.sporteventapp.backend.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.repositories.SportEventRepository;
import hu.szeged.sporteventapp.common.exception.AlreadyJoinedException;
import hu.szeged.sporteventapp.common.exception.NoEmptyPlaceException;
import hu.szeged.sporteventapp.common.exception.NotParticipantException;

@RunWith(MockitoJUnitRunner.class)
public class SportEventServiceTest {

	@Mock
	SportEventRepository repository;
	@Mock
	SportEvent sportEvent;
	@Mock
	User user1;
	@Mock
	User user2;
	SportEventService sportEventService;

	@Before
	public void setUp() throws Exception {
		sportEventService = new SportEventService(repository);
	}

	@Test
	public void shouldJoinToSportEvent() throws Exception {
		try {
			Set set = new LinkedHashSet<>();
			set.add(user1);
			when(sportEvent.getParticipants()).thenReturn(set);
			when(sportEvent.getMaxParticipant()).thenReturn(2);
			sportEventService.joinToSportEvent(sportEvent, user2);
		} catch (Exception e) {
			fail("Not should throw any exception");
		}
	}

	@Test(expected = AlreadyJoinedException.class)
	public void alreadyJoinToSportEvent() throws Exception {
		Set set = new LinkedHashSet<>();
		set.add(user2);
		when(sportEvent.getParticipants()).thenReturn(set);
		when(sportEvent.getMaxParticipant()).thenReturn(2);
		sportEventService.joinToSportEvent(sportEvent, user2);
	}

	@Test(expected = NoEmptyPlaceException.class)
	public void noEmptyPlaceJoinToSportEvent() throws Exception {
		Set set = new LinkedHashSet<>();
		set.add(user2);
		when(sportEvent.getParticipants()).thenReturn(set);
		when(sportEvent.getMaxParticipant()).thenReturn(0);
		sportEventService.joinToSportEvent(sportEvent, user2);
	}

	@Test
	public void shouldLeaveFromSportEvent() throws Exception {
		Set set = new LinkedHashSet<>();
		set.add(user2);
		set.add(user1);
		when(sportEvent.getParticipants()).thenReturn(set);
		sportEventService.leaveFromSportEvent(sportEvent, user2);
	}

	@Test(expected = NotParticipantException.class)
	public void shouldNotLeaveFromSportEvent() throws Exception {
		Set set = new LinkedHashSet<>();
		set.add(user2);
		when(sportEvent.getParticipants()).thenReturn(set);
		sportEventService.leaveFromSportEvent(sportEvent, user1);
	}
}