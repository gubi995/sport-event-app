package hu.szeged.sporteventapp.backend.data.entity;

import java.util.Set;

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
@Table(name = "message_board")
public class MessageBoard extends AbstractEntity {

	@OneToMany(mappedBy = "containingMessageBoard", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	Set<Message> messages;

	@OneToOne(mappedBy = "messageBoard")
	SportEvent sportEvent;
}
