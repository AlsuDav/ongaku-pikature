ALTER TABLE ongaku_pikature.author ADD CONSTRAINT unique_author_name UNIQUE (name);
ALTER TABLE ongaku_pikature.music ADD CONSTRAINT unique_music_name UNIQUE (name);
ALTER TABLE ongaku_pikature.author_music ADD CONSTRAINT unique_music_author UNIQUE (author_id, music_id),
    ALTER COLUMN author_id SET NOT NULL,
    ALTER COLUMN music_id SET NOT NULL;
