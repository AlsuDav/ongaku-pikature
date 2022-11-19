package ru.itis.ongakupikature.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.SignUpDto;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.UsersRepository;

import java.time.LocalDate;

@Service
public class SignUpServiceImpl implements SignUpService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                .build();
        usersRepository.save(user);
    }
}
