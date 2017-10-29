package hu.szeged.sporteventapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.szeged.sporteventapp.backend.data.entity.Picture;

public interface PictureRepository extends JpaRepository<Picture, String> {
}
