package hu.szeged.sporteventapp.backend.data.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sport_event")
public class SportEvent extends AbstractEntity {

	@Column(nullable = false, length = 40)
	private String name;

	@Column(nullable = false)
	private String location;

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	@Column(name = "max_participant")
	private Integer maxParticipant;

	@Basic
	private String details;

	@Column(name = "organizer", nullable = false)
	private User organizer;

	@Column(name = "participants")
	private List<User> participants;

}
