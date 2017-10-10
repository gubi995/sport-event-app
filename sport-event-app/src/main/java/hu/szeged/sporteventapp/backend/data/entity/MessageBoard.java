package hu.szeged.sporteventapp.backend.data.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "message_board")
public class MessageBoard extends AbstractEntity {

	@OneToMany(mappedBy = "containingMessageBoard")
	List<Message> messages;

	@OneToOne(mappedBy = "messageBoard")
	SportEvent sportEvent;
}
