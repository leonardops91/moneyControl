package br.com.challenge.moneycontrol.repository;

import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.model.Outcome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface OutcomeRepository extends JpaRepository<Outcome, Integer> {
    Page<Outcome> findByDescriptionIgnoreCaseContaining(String description,
                                        Pageable pagination);

    Page<Outcome> findByDateBetween(LocalDate initialDate, LocalDate finalDate, Pageable pagination);

    Page<Outcome> findByCategoryAndDateBetween(OutcomeCategory category,
                                               LocalDate initialDate, LocalDate finalDate, Pageable unpaged);
}
