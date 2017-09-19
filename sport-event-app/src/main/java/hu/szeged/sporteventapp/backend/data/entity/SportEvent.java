package hu.szeged.sporteventapp.backend.data.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

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

	@Column(name = "sport_type", nullable = false)
	private String sportType;

	@Column(columnDefinition = "POINT")
	private Point point;

	@Column(columnDefinition = "POLYGON")
	private LineString route;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User organizer;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "participate_in_event",
			joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private List<User> participants;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "album_id")
	private Album album;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "message_board_id")
	private MessageBoard messageBoard;
}
