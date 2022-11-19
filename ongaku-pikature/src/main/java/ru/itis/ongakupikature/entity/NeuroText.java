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
@Table(schema = "ongaku_pikature", name = "neuro_text")
public class NeuroText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "music_id", nullable = false)
    private Music music;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_key_words")
    private String userKeyWords;

    @Column(name = "user_picture_path")
    private String userPicturePath;

    @Column(name = "auto_picture_path")
    private String autoPicturePath;

}
