package hu.szeged.sporteventapp.backend.data.entity;

import javax.persistence.*;

import org.hibernate.validator.constraints.Email;

import hu.szeged.sporteventapp.backend.data.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends AbstractEntity {

	@Column(nullable = false, length = 25, unique = true)
	private String username;

	@Email
	@Column(nullable = false, length = 25, unique = true)
	private String email;

	@Column(nullable = false, length = 100)
	private String password;

	@Column(name = "picture_name")
	private String pictureName;

	@Basic
	@Enumerated(value = EnumType.STRING)
	private Role role;

	@Basic
	private int age;

	@Column(name = "real_name", length = 40)
	private String realName;

	@Column(name = "mobil_number", length = 20)
	private String mobileNumber;

	@OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
	private List<SportEvent> organizedEvents;
}
