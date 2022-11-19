package ru.itis.ongakupikature.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
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

}
