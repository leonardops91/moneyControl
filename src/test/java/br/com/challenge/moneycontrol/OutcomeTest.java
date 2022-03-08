package br.com.challenge.moneycontrol;

import br.com.challenge.moneycontrol.controller.OutcomeController;
import br.com.challenge.moneycontrol.controller.UserController;
import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.form.OutcomeForm;
import br.com.challenge.moneycontrol.model.Outcome;
import br.com.challenge.moneycontrol.model.UserAccount;
import br.com.challenge.moneycontrol.repository.OutcomeRepository;

import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
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
@ContextConfiguration(classes = {
        OutcomeController.class
})
public class OutcomeTest {
    private final String BASE_URL = "/despesas";

    Pageable pagination = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

    private UserAccount user = new UserAccount(
            1L,
            "teste",
            "teste@teste.com",
            "12345"
    );

    private ObjectMapper objectMapper;

    @Autowired
    private OutcomeController outcomeController;

    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    @MockBean
    private OutcomeRepository outcomeRepository;

    @Before
    public void setUp() {
        when(userController.currentUser()).thenReturn(user);
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders
                .standaloneSetup(outcomeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void deveRetornarDespesasDoUsuarioComPaginacao() throws Exception {
        Outcome outcome = new Outcome("teste", 225, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras, user);
        Outcome outcome1 = new Outcome("teste2", 225, LocalDate.of(2022, 12,
                18), Type.Variável, OutcomeCategory.Alimentação, user);
        List<Outcome> list = new ArrayList<>();
        list.add(outcome);
        list.add(outcome1);
        Page<Outcome> outcomes = new PageImpl(list, pagination, 2);

        when(outcomeRepository.findByUser(pagination, user)).thenReturn(outcomes);

        mockMvc.perform(get(BASE_URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numberOfElements", is(2)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(status().isOk());
    }

    @Test
    public void deveRetornarOutcomePeloID() throws Exception {
        Outcome outcome = new Outcome("teste", 400, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras, user);
        Outcome outcome1 = new Outcome("teste2", 225, LocalDate.of(2022, 12,
                18),
                Type.Variável,
                OutcomeCategory.Alimentação, user);

        when(outcomeRepository.findById(2)).thenReturn(Optional.of(outcome1));


        mockMvc.perform(get(BASE_URL + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("teste2")))
                .andExpect(jsonPath("$.value", is(225.0)));
        verify(outcomeRepository, times(1)).findById(2);
    }

    @Test
    public void deveRetornarErro404NotFoundQuandoNaoEncontrarParaRetornar() throws Exception {
        Outcome outcome = new Outcome("teste", 400, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras, user);
        Outcome outcome1 = new Outcome("teste2", 225, LocalDate.of(2022, 12,
                18),
                Type.Variável,
                OutcomeCategory.Alimentação, user);

        when(outcomeRepository.findById(2)).thenReturn(Optional.of(outcome1));


        mockMvc.perform(get(BASE_URL + "/3"))
                .andExpect(status().isNotFound());
        verify(outcomeRepository, times(1)).findById(3);
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
        outcome.setUser(user);

        when(outcomeRepository.save(any(Outcome.class))).thenReturn(outcome);

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
        outcome.setUser(user);

        when(outcomeRepository.findById(1)).thenReturn(Optional.of(outcome));
        form.setValue(500);

        when(outcomeRepository.getById(1)).thenReturn(outcome);
        when(outcomeRepository.save(any(Outcome.class))).thenReturn(outcome);

        mockMvc.perform(put(BASE_URL + "/1").content(objectMapper.writeValueAsString(form)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(500.0)));
    }

    @Test
    public void deveDeletarUmRegistroQuandoRequisitado() throws Exception {
        Outcome outcome = new Outcome("teste", 225, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras, user);

        when(outcomeRepository.findById(1)).thenReturn(Optional.of(outcome));

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isOk());
        verify(outcomeRepository, times(1)).deleteById(1);
    }

    @Test
    public void deveRetornarErro404NotFoundQuandoNaoEncontrarParaDeletar() throws Exception {
        Outcome outcome = new Outcome("teste", 225, LocalDate.of(2022, 12, 10),
                Type.Fixa,
                OutcomeCategory.Outras, user);

        when(outcomeRepository.findById(1)).thenReturn(Optional.of(outcome));

        mockMvc.perform(delete(BASE_URL + "/2"))
                .andExpect(status().isNotFound());
        verify(outcomeRepository, times(1)).findById(2);
    }

}
