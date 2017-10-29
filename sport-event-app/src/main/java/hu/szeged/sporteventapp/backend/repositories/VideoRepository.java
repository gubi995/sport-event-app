package hu.szeged.sporteventapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.szeged.sporteventapp.backend.data.entity.Video;

public interface VideoRepository extends JpaRepository<Video, String> {
}
