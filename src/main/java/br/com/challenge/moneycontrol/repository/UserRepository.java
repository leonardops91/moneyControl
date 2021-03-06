package br.com.challenge.moneycontrol.repository;

import br.com.challenge.moneycontrol.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByEmail(String username);
}
