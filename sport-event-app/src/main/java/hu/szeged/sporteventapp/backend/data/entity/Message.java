package hu.szeged.sporteventapp.backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
