package hu.szeged.sporteventapp.backend.data.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message extends AbstractEntity {

	@Basic
	String text;

	@ManyToOne(optional = false)
	@JoinColumn(name = "message_board_id")
	MessageBoard containingMessageBoard;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	User user;
}
