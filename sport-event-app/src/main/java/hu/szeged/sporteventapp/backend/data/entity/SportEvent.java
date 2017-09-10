package hu.szeged.sporteventapp.backend.data.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

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

	@Column(name = "sport_type", nullable = false)
	private String sportType;

	@Column(name = "is_full")
	@Type(type = "yes_no")
	private Boolean isFull;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User organizer;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "participants_on_event",
			joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private Set<User> participants;

}
