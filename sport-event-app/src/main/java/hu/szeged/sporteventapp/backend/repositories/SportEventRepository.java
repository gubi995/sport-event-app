package hu.szeged.sporteventapp.backend.repositories;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SportEventRepository extends JpaRepository<SportEvent, String> {
    List<SportEvent> findAll();

    List<SportEvent> findSportEventByOrganizer(User user);
}
