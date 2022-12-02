package ru.itis.ongakupikature.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(schema = "ongaku_pikature", name = "music")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "text")
    private String text;

    @Column(name = "genre")
    private String genre;

    @Column(name = "auto_key_words")
    private String autoKeyWords;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "poster_path")
    private String posterPath;

    @ManyToMany(mappedBy = "musicList")
    private List<Author> authors;

}
