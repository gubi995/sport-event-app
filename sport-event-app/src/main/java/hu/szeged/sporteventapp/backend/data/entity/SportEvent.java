package hu.szeged.sporteventapp.backend.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class SportEvent extends AbstractEntity {

	@NotNull
	@NotEmpty
	@Size(max = 30)
	private String name;

	@NotNull
	@NotEmpty
	private String location;

	@NotNull
	@NotEmpty
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date startDate;

	@NotNull
	@NotEmpty
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date endDate;

	@NotNull
	@NotEmpty
	private String details;

	@NotNull
	@NotEmpty
	private String organizerName;

	public SportEvent(String name, String location, Date startDate, Date endDate,
			String details, String organizerName) {
		this.name = name;
		this.location = location;
		this.startDate = startDate;
		this.endDate = endDate;
		this.details = details;
		this.organizerName = organizerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getOrganizerName() {
		return organizerName;
	}

	public void setOrganizerName(String organizerName) {
		this.organizerName = organizerName;
	}
}
