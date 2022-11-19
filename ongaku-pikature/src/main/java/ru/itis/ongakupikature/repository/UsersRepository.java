package ru.itis.ongakupikature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.ongakupikature.entity.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

}
