package ru.itis.ongakupikature.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.UsersRepository;

import java.util.Optional;

@Service(value = "customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UsersRepository usersRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Optional<User> userOptional = usersRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                return new UserDetailsImpl(userOptional.get());
            }
            throw new UsernameNotFoundException("User not found");
        }

}
