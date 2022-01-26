package br.com.challenge.moneycontrol.repository;
import br.com.challenge.moneycontrol.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface IncomeRepository extends JpaRepository<Income, Integer> {
    Page<Income> findByDescriptionIgnoreCaseContaining(String description,
                                                       Pageable pagination);
    Page<Income> findByDateBetween(LocalDate initialDate,
                                      LocalDate finalDate,
                                      Pageable pagination);
}
