package pl.kurs.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kurs.model.User;
import pl.kurs.model.UserDetailsImpl;
import pl.kurs.repository.UserRepository;

import java.text.MessageFormat;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format(
                        "User with email {0} not found.", email)));
    }
}
