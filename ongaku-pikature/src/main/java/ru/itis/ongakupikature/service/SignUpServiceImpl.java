package ru.itis.ongakupikature.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.SignUpDto;
import ru.itis.ongakupikature.entity.Playlist;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.PlaylistRepository;
import ru.itis.ongakupikature.repository.UsersRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private static final String FAVORITE_PLAYLIST_NAME = "Избранное";

    private final UsersRepository usersRepository;
    private final PlaylistRepository playlistRepository;

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
                .build();
        usersRepository.save(user);
        var favoritePlaylist = Playlist.builder()
                .name(FAVORITE_PLAYLIST_NAME)
                .user(user)
                .dateCreate(LocalDate.now())
                .build();
        playlistRepository.save(favoritePlaylist);
        user.setFavoritePlaylistId(favoritePlaylist.getId());
        usersRepository.save(user);
    }
}