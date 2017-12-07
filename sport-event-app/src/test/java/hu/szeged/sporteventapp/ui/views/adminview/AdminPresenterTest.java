package hu.szeged.sporteventapp.ui.views.adminview;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.data.enums.Role;
import hu.szeged.sporteventapp.backend.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class AdminPresenterTest {

	@Mock
	UserService userService;
	@Mock
	User user1;
	@Mock
	User user2;
	@Mock
	User admin;

	Set<User> users;
	AdminPresenter presenter;

	@Before
	public void setUp() throws Exception {
		presenter = new AdminPresenter(userService);
	}

	@Test
	public void shouldGetUsersWithoutAdmins() throws Exception {
		Set<User> users = new LinkedHashSet<>();
		users.add(user1);
		users.add(user2);
		users.add(admin);

		when(user1.getRole()).thenReturn(Role.USER);
		when(user2.getRole()).thenReturn(Role.USER);
		when(admin.getRole()).thenReturn(Role.ADMIN);
		when(userService.findAll()).thenReturn(users);

		Set<User> expected = new LinkedHashSet<>();
		expected.add(user1);
		expected.add(user2);
		assertEquals(presenter.getUsers(), expected);
	}
}