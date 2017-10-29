package hu.szeged.sporteventapp.backend.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "video")
public class Video extends MediaType {

	public Video() {
	}

	public Video(String name, Album album) {
		super(name, album);
	}
}
