package br.com.challenge.moneycontrol.controller;

import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.model.Outcome;
import br.com.challenge.moneycontrol.repository.IncomeRepository;
import br.com.challenge.moneycontrol.repository.OutcomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resumo")
public class ResumeController {
    private double totalIncome;
    private double totalOutcome;
    private double balance;
    private Map<String, Double> outcomeByCategory = new HashMap<>();

    @Autowired
    OutcomeRepository outcomeRepository;
    @Autowired
    IncomeRepository incomeRepository;

    @GetMapping("/{year}/{month}")
    public Map<String,Map<String, Double>> test(@PathVariable int year,
                                     @PathVariable int month) {
        LocalDate initialDate = LocalDate.of(year, month, 1);
        LocalDate finalDate = LocalDate.of(year, month, initialDate.lengthOfMonth());
        Map<String, Double> totalBalance = new HashMap<>();
        setTotalOutcome(initialDate, finalDate);
        setTotalIncome(initialDate, finalDate);
        setBalance();
        totalBalance.put("TotalReceitas", totalIncome);
        totalBalance.put("TotalDespesas", totalOutcome);
        totalBalance.put("SaldoPeriodo", balance);

        setOutcomeByCategory(initialDate, finalDate);

        Map<String, Map<String, Double>> resume = new HashMap<>();

        resume.put("TotalGeral", totalBalance);
        resume.put("DespesaPorCategoria", outcomeByCategory);

        return resume;
    }

    public void setTotalOutcome(LocalDate initialDate, LocalDate finalDate) {
        Page<Outcome> outcomes = outcomeRepository.findByDateBetween(initialDate,
                finalDate, Pageable.unpaged());
        this.totalOutcome = outcomes.stream().mapToDouble(Outcome::getValue).sum();
    }

    public void setTotalIncome(LocalDate initialDate, LocalDate finalDate) {
        Page<Income> incomes = incomeRepository.findByDateBetween(initialDate,
                finalDate, Pageable.unpaged());
        this.totalIncome = incomes.stream().mapToDouble(Income::getValue).sum();
    }

    public void setBalance() {
        this.balance = totalIncome - totalOutcome;
    }

    public void setOutcomeByCategory( LocalDate initialDate,
                                                     LocalDate finalDate) {
        List<OutcomeCategory> list = Arrays.asList(OutcomeCategory.values());
        for(int i = 0; i < list.size(); i++){
            OutcomeCategory category = list.get(i);
           Page<Outcome> outcomesByCategory=
                    outcomeRepository.findByCategoryAndDateBetween(category,
                    initialDate, finalDate, Pageable.unpaged());
           double sumOfCategory =
                   outcomesByCategory.stream().mapToDouble(Outcome::getValue).sum();
           this.outcomeByCategory.put(category.toString(), sumOfCategory);
        }
    }


}
