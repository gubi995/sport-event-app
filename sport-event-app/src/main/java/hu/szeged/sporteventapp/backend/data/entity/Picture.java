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
@Table(name = "picture")
public class Picture extends MediaType {

	public Picture() {
	}

	public Picture(String name, Album album) {
		super(name, album);
	}
}
