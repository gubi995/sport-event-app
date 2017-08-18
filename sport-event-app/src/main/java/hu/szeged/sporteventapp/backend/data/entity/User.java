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
	@Size(max = 25)
	private String username;

	@Email
	@NotNull
	@NotEmpty
	@Size(max = 25)
	private String email;

	@NotNull
	@NotEmpty
	@Size(max = 100)
	private String password;

	@Size(max = 255)
	private String pictureName;

	@NotNull
	@Size(max = 15)
	private String role;

	@NotNull
	@Size(max = 15)
	private String state;

	private int age;

	@Size(max = 30)
	private String realName;

	@Size(max = 30)
	private String mobileNumber;

	public User() {
		// An empty constructor is needed for all beans
	}

	public User(String username, String email, String password, String pictureName,
			String role, String state) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.pictureName = pictureName;
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

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}
