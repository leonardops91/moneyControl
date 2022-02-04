package br.com.challenge.moneycontrol;

import br.com.challenge.moneycontrol.controller.OutcomeController;
import br.com.challenge.moneycontrol.controller.ResumeController;
import br.com.challenge.moneycontrol.enumerable.IncomeCategory;
import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.model.Outcome;
import br.com.challenge.moneycontrol.repository.IncomeRepository;
import br.com.challenge.moneycontrol.repository.OutcomeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.in;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ResumeController.class)
public class ResumeControllerTest {
    private String BASE_URL = "/resumo";

    @Autowired
    private ResumeController resumeController;

    @MockBean
    private IncomeRepository incomeRepository;

    @MockBean
    private OutcomeRepository outcomeRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(resumeController).build();
    }

    @Test
    public void deveRetornarResumoFinanceiroQuandoRequisitado() throws Exception{
        LocalDate initialDate = LocalDate.of(2022, 1, 1);
        LocalDate finalDate = LocalDate.of(2022, 1,
                initialDate.lengthOfMonth());

        Outcome outcome = new Outcome("teste2", 225, LocalDate.of(2022, 12,
                18),
                Type.Variável,
                OutcomeCategory.Alimentação);
        Income income = new Income("Teste", 225, LocalDate.of(2022, 12, 1),
                Type.Fixa,
                IncomeCategory.Dividendos);

        List<Outcome> listOfOutcomes = new ArrayList<>();
        List<Income> listOfIncomes = new ArrayList<>();

        listOfOutcomes.add(outcome);
        listOfIncomes.add(income);

        Page<Outcome> pageOfOutcomes = new PageImpl(listOfOutcomes,
                Pageable.unpaged(),
                1);
        Page<Income> pageOfIncomes = new PageImpl(listOfIncomes,
                Pageable.unpaged(),
                1);

        when(outcomeRepository.findByDateBetween(initialDate, finalDate,
                Pageable.unpaged())).thenReturn(pageOfOutcomes);
        when(incomeRepository.findByDateBetween(initialDate, finalDate,
                Pageable.unpaged())).thenReturn(pageOfIncomes);
        List<OutcomeCategory> list = Arrays.asList(OutcomeCategory.values());
        for (int i = 0; i<list.size(); i++ ){
            OutcomeCategory category = list.get(i);
            when(outcomeRepository.findByCategoryAndDateBetween(category,
                    initialDate, finalDate, Pageable.unpaged()
            )).thenReturn(pageOfOutcomes);
        }

        mockMvc.perform(get(BASE_URL + "/2022/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.TotalGeral.SaldoPeriodo", is(0.0)))
                .andExpect(jsonPath("$.DespesaPorCategoria.Alimentação",
                        is(225.0)));
        verify(outcomeRepository, times(1)).findByDateBetween(initialDate,
                finalDate, Pageable.unpaged());
        verify(incomeRepository, times(1)).findByDateBetween(initialDate,
                finalDate, Pageable.unpaged());
        verify(outcomeRepository, times(1)).findByCategoryAndDateBetween(OutcomeCategory.Alimentação, initialDate, finalDate, Pageable.unpaged());

    }
}
