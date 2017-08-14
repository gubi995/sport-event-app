package hu.szeged.sporteventapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.szeged.sporteventapp.backend.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User save(User user);

	int countByUsername(String username);

	int countByEmail(String email);
}
