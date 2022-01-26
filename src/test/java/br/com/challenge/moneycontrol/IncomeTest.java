package br.com.challenge.moneycontrol;


import br.com.challenge.moneycontrol.enumerable.IncomeCategory;
import org.junit.Assert;
import org.junit.Test;

import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;

import java.time.LocalDate;

public class IncomeTest {

	@Test
	public void deveRetonarValorDaReceitaCriada() {
		Income income = new Income("Teste", 210, LocalDate.of(2022, 12, 1),
				Type.fixed,
				IncomeCategory.Dividendos);
		double valorEsperado = 210;
		Assert.assertEquals(income.getValue(), valorEsperado, 0.1);
	}
}
