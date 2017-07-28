package hu.szeged.sporteventapp.backend.repositories;

import hu.szeged.sporteventapp.backend.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
