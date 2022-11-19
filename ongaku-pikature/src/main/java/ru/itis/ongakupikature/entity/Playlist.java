package ru.itis.ongakupikature.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(schema = "ongaku_pikature", name = "playlist")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @ManyToMany
    @JoinTable(name = "playlist_music",  schema = "ongaku_pikature",
            joinColumns = {@JoinColumn(name = "playlist_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "music_id", referencedColumnName = "id")})
    private List<Music> musicList;

}
