package ru.itis.ongakupikature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.ongakupikature.entity.NeuroText;

public interface NeuroTextRepository extends JpaRepository<NeuroText, Long> {
}
