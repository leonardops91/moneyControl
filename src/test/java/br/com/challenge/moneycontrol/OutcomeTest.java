package br.com.challenge.moneycontrol;

import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.model.Outcome;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public class OutcomeTest {

    @Test
    public void deveRetornarValorTotalDeIncomes(){
        Outcome income = new Outcome("teste", 225, LocalDate.of(2022, 12, 10),
                Type.fixed,
                OutcomeCategory.Outras);
        Outcome income2 = new Outcome("teste2", 225, LocalDate.of(2022, 12, 18),
                Type.variable,
                OutcomeCategory.Alimentação);
        double expected = 450;
        double achieved = Outcome.totalOutcome(2022, 12, Pageable.unpaged());

        Assert.assertEquals(expected, achieved, 0.01);
    }
}
