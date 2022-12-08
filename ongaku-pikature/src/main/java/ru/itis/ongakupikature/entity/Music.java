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
@NamedEntityGraph(name = "music.authors",
        attributeNodes = {@NamedAttributeNode("authors")})
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

    @ManyToMany
    @JoinTable(name = "author_music", schema = "ongaku_pikature",
            joinColumns = {@JoinColumn(name = "music_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id", referencedColumnName = "id")})
    private List<Author> authors;

}
