package hu.szeged.sporteventapp.backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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
