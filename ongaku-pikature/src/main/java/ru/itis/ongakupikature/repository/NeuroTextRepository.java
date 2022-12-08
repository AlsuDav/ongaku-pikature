package ru.itis.ongakupikature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.ongakupikature.entity.NeuroText;
import ru.itis.ongakupikature.entity.User;

public interface NeuroTextRepository extends JpaRepository<NeuroText, Long> {

    NeuroText findByMusicIdAndUser(Long musicId, User user);
}
