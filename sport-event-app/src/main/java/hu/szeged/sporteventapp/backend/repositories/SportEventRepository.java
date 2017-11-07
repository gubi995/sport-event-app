package hu.szeged.sporteventapp.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;

public interface SportEventRepository extends JpaRepository<SportEvent, String> {

	List<SportEvent> findAll();

	List<SportEvent> findSportEventByStartDateGreaterThanEqualAndEndDateLessThanEqual(LocalDateTime startDate,
			LocalDateTime endDate);

	List<SportEvent> findSportEventByOrganizer(User user);

	List<SportEvent> findSportEventBySportType(String sportType);
}
