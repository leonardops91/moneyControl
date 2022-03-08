package br.com.challenge.moneycontrol.config.security;

import br.com.challenge.moneycontrol.model.UserAccount;
import br.com.challenge.moneycontrol.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new UsernameNotFoundException("Dados inválidos");
    }
}
