alter table ongaku_pikature.music
    add column if not exists file_path varchar (150),
    add column if not exists poster_path varchar (150);

alter table ongaku_pikature.user
    add column if not exists favorite_playlist_id bigint
    references ongaku_pikature.playlist (id);
