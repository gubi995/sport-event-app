package hu.szeged.sporteventapp.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class User extends AbstractEntity {

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String username;

	@Email
	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String email;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String password;

	@Size(max = 255)
	private String picturePath;

	@NotNull
	@Size(max = 255)
	private String role;

	@NotNull
	@Size(max = 255)
	private String state;

	public User() {
		// An empty constructor is needed for all beans
	}

	public User(String username, String email, String password, String picturePath,
			String role, String state) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.picturePath = picturePath;
		this.role = role;
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
