package ru.itis.ongakupikature.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.ongakupikature.entity.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    @NonNull
    Optional<User> findById(@NonNull Long id);

    Optional<User> findByEmail(String email);

}
