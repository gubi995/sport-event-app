package hu.szeged.sporteventapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.szeged.sporteventapp.backend.data.entity.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
}