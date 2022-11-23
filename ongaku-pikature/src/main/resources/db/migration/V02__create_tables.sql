create type ongaku_pikature.user_role as enum ('ADMIN', 'USER');

create table ongaku_pikature.user
(
    id           bigserial primary key,
    phone_number VARCHAR(12),
    email        VARCHAR(50) UNIQUE,
    password     VARCHAR(500) NOT NULL,
    login        VARCHAR(50),
    role         ongaku_pikature.user_role,
    is_active    BOOLEAN,
    sign_up_date DATE,
    photo_path   VARCHAR(300)
);

create table ongaku_pikature.music
(
    id    bigserial primary key,
    name  VARCHAR(100),
    text  TEXT,
    genre VARCHAR(50),
    auto_key_words text

);

create table ongaku_pikature.author
(
    id        bigserial primary key,
    name      VARCHAR(100),
    biography TEXT
);

create table ongaku_pikature.author_music
(
    id        bigserial primary key,
    music_id  bigint,
    author_id bigint,
    FOREIGN KEY (music_id) REFERENCES ongaku_pikature.music (id),
    FOREIGN KEY (author_id) REFERENCES ongaku_pikature.author (id)
);

create table ongaku_pikature.neuro_text
(
    id       bigserial primary key,
    music_id bigint,
    user_id  bigint,
    user_key_words text,
    user_picture_path varchar(200),
    auto_picture_path varchar(200),
    FOREIGN KEY (user_id) REFERENCES ongaku_pikature.user (id),
    FOREIGN KEY (music_id) REFERENCES ongaku_pikature.music (id)
);

create table ongaku_pikature.playlist
(
    id          bigserial primary key,
    name        VARCHAR(50) NOT NULL,
    user_id     bigint,
    FOREIGN KEY (user_id) REFERENCES ongaku_pikature.user (id),
    date_create DATE

);

create table ongaku_pikature.playlist_music
(
    id          bigserial primary key,
    music_id    bigint,
    playlist_id bigint,
    FOREIGN KEY (playlist_id) REFERENCES ongaku_pikature.playlist (id),
    FOREIGN KEY (music_id) REFERENCES ongaku_pikature.music (id)
);