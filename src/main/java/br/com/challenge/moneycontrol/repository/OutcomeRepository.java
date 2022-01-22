package br.com.challenge.moneycontrol.repository;

import br.com.challenge.moneycontrol.model.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutcomeRepository extends JpaRepository<Outcome, Integer> {
}
