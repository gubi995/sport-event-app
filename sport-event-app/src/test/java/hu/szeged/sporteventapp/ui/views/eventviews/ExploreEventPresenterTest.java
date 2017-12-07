package hu.szeged.sporteventapp.ui.views.eventviews;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.service.SportEventService;
import hu.szeged.sporteventapp.backend.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class ExploreEventPresenterTest {

	@Mock
	UserService userService;
	@Mock
	SportEvent sportEvent;
	@Mock
	SportEventService sportEventService;

	ExploreEventPresenter presenter;

	@Before
	public void setUp() throws Exception {
		presenter = new ExploreEventPresenter(sportEventService, userService);
		presenter.setView(mock(ExploreEventView.class));
	}

	@Test
	public void shouldLeaveSuccessful() {
		try {
			presenter.leave(sportEvent);
		} catch (Exception e) {
			fail("Not expected any exception");
		}
	}

	@Test
	public void shouldJoinSuccessful() throws Exception {
		try {
			presenter.join(sportEvent);
		} catch (Exception e) {
			fail("Not expected any exception");
		}
	}
}