package br.com.challenge.moneycontrol;


import org.junit.Assert;
import org.junit.Test;

import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;

public class IncomeTest {

	@Test
	public void deveRetonarValorDaReceitaCriada() {
		Income income = new Income("Teste", 210, null, Type.fixed);
		double valorEsperado = 210;
		Assert.assertEquals(income.getValue(), valorEsperado, 0.1);
	}
}
