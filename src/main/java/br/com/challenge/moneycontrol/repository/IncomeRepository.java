package br.com.challenge.moneycontrol.repository;
import br.com.challenge.moneycontrol.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IncomeRepository extends JpaRepository<Income, Integer> {
}
