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
@Table(name = "album")
public class Album extends AbstractEntity{

    @Basic
    String name;

    @Basic
    @OneToMany(mappedBy = "album")
    List<Picture> pictures;

    @Basic
    @OneToMany(mappedBy = "album")
    List<Video> videos;

    @OneToOne(mappedBy = "album")
    SportEvent sportEvent;
}
