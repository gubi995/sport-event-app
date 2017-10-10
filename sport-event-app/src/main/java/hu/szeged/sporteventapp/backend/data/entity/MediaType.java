package hu.szeged.sporteventapp.backend.data.entity;

import javax.persistence.Basic;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class MediaType extends AbstractEntity {

	@Basic
	String name;

	@Basic
	String path;

	@ManyToOne(optional = false)
	@JoinColumn(name = "album_id")
	Album album;
}
