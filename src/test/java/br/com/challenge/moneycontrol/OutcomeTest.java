package br.com.challenge.moneycontrol;

import br.com.challenge.moneycontrol.controller.OutcomeController;
import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.form.OutcomeForm;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.model.Outcome;
import br.com.challenge.moneycontrol.repository.OutcomeRepository;

import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(OutcomeController.class)
public class OutcomeTest {
    private final String BASE_URL = "/despesas";

    private ObjectMapper objectMapper;

    @Autowired
    private OutcomeController outcomeController;

    private MockMvc mockMvc;


    @MockBean
    private OutcomeRepository mockRepository;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders
                .standaloneSetup(outcomeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void deveRetornarPaginaDeRespostas() throws Exception {
        Outcome outcome = new Outcome("teste", 225, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras);
        Outcome outcome1 = new Outcome("teste2", 225, LocalDate.of(2022, 12,
                18),
                Type.Variável,
                OutcomeCategory.Alimentação);
        List<Outcome> outcomes = new ArrayList<>();
        outcomes.add(outcome);
        outcomes.add(outcome1);
        Pageable pagination = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Page<Outcome> outcomePage = new PageImpl(outcomes, pagination, 10);

        when(mockRepository.findAll(pagination)).thenReturn(
                outcomePage);

        mockMvc.perform(get("/despesas").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements", is(2)));
    }

    @Test
    public void deveRetornarOutcomePeloID() throws Exception {
        Outcome outcome = new Outcome("teste", 225, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras);
        Outcome outcome1 = new Outcome("teste2", 225, LocalDate.of(2022, 12,
                18),
                Type.Variável,
                OutcomeCategory.Alimentação);

        when(mockRepository.findById(2)).thenReturn(Optional.of(outcome1));


        mockMvc.perform(get(BASE_URL + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("teste2")));
    }

    @Test
    public void deveRetornarErro404NotFoundQuandoNaoEncontrarParaRetornar() throws Exception {
        Outcome outcome = new Outcome("teste", 225, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras);
        Outcome outcome1 = new Outcome("teste2", 225, LocalDate.of(2022, 12,
                18),
                Type.Variável,
                OutcomeCategory.Alimentação);

        when(mockRepository.findById(2)).thenReturn(Optional.of(outcome1));


        mockMvc.perform(get(BASE_URL + "/3"))
                .andExpect(status().isNotFound());
        verify(mockRepository, times(1)).findById(3);
    }

    @Test
    public void deveInserirDespesaNoBD() throws Exception {
        OutcomeForm form = new OutcomeForm();
        form.setDescription("Teste");
        form.setValue(1000);
        form.setDate(LocalDate.of(2022, 12, 10));
        form.setType(Type.Fixa);
        form.setCategory(OutcomeCategory.Alimentação);
        Outcome outcome = form.convert();

        when(mockRepository.save(any(Outcome.class))).thenReturn(outcome);

        mockMvc.perform(post(BASE_URL)
                        .content(objectMapper.writeValueAsString(form))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("Teste")))
                .andExpect(jsonPath("$.value", is(1000.0)))
                .andExpect(jsonPath("$.category", is("Alimentação")));
    }

    @Test
    public void deveAtualizarUmaDespesaQuandoRequisitado() throws Exception {
        OutcomeForm form = new OutcomeForm();
        form.setDescription("Teste");
        form.setValue(1000);
        form.setDate(LocalDate.of(2022, 12, 10));
        form.setType(Type.Fixa);
        form.setCategory(OutcomeCategory.Alimentação);
        Outcome outcome = form.convert();

        when(mockRepository.findById(1)).thenReturn(Optional.of(outcome));
        outcome.setValue(500);

        when(mockRepository.getById(1)).thenReturn(outcome);
        when(mockRepository.save(any(Outcome.class))).thenReturn(outcome);

        mockMvc.perform(put(BASE_URL + "/1").content(objectMapper.writeValueAsString(outcome)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(500.0)));
    }

    @Test
    public void deveDeletarUmRegistroQuandoRequisitado() throws Exception {
        Outcome outcome = new Outcome("teste", 225, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras);

        when(mockRepository.findById(1)).thenReturn(Optional.of(outcome));

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isOk());
        verify(mockRepository, times(1)).deleteById(1);
    }

    @Test
    public void deveRetornarErro404NotFoundQuandoNaoEncontrarParaDeletar() throws Exception {
        Outcome outcome = new Outcome("teste", 225, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras);

        when(mockRepository.findById(1)).thenReturn(Optional.of(outcome));

        mockMvc.perform(delete(BASE_URL + "/2"))
                .andExpect(status().isNotFound());
        verify(mockRepository, times(1)).findById(2);
    }
}
