package hu.szeged.sporteventapp.backend.data.entity;

import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "album")
public class Album extends AbstractEntity {

	@Basic
	String name;

	@Basic
	@OneToMany(mappedBy = "album")
	Set<Picture> pictures;

	@Basic
	@OneToMany(mappedBy = "album")
	Set<Video> videos;

	@OneToOne(mappedBy = "album")
	SportEvent sportEvent;
}
