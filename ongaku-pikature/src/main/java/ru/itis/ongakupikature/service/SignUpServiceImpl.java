package ru.itis.ongakupikature.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.SignUpDto;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.UsersRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    @Value("${profile.image.path}")
    private String defaultImagePath;

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpDto form) {
        User user = User.builder()
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .login(form.getEmail())
                .signUpDate(LocalDate.now())
                .phoneNumber(form.getPhoneNumber())
                .isActive(Boolean.TRUE)
                .role(User.Role.USER)
                .photoPath(defaultImagePath)
                .build();
        usersRepository.save(user);
    }
}