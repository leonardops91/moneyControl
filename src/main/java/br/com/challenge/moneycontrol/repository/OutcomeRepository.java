package br.com.challenge.moneycontrol.repository;

import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.model.Outcome;
import br.com.challenge.moneycontrol.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface OutcomeRepository extends JpaRepository<Outcome, Integer> {
//    Page<Outcome> findByDateBetween(LocalDate initialDate, LocalDate finalDate, Pageable pagination);

    Page<Outcome> findByUser(Pageable pagination, UserAccount user);

    Page<Outcome> findByUserAndDescriptionIgnoreCaseContaining(String descricao, Pageable pagination, UserAccount currentUser);

    Page<Outcome> findByUserAndDateBetween(UserAccount currentUser, LocalDate initialDate, LocalDate finalDate, Pageable pagination);

    Page<Outcome> findByUserAndCategoryAndDateBetween(UserAccount currentUser,
                                                    OutcomeCategory category, LocalDate initialDate, LocalDate finalDate, Pageable unpaged);
}
