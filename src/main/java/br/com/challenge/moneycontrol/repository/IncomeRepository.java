package br.com.challenge.moneycontrol.repository;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface IncomeRepository extends JpaRepository<Income, Integer> {
    Page<Income> findByDateBetween(LocalDate initialDate,
                                      LocalDate finalDate,
                                      Pageable pagination);



    Page<Income> findByUser(UserAccount currentUser, Pageable pagination);

    Page<Income> findByUserAndDescriptionIgnoreCaseContaining(UserAccount currentUser, String descricao, Pageable pagination);

    Page<Income> findByUserAndDateBetween(UserAccount currentUser, LocalDate initialDate, LocalDate finalDate, Pageable pagination);
}
