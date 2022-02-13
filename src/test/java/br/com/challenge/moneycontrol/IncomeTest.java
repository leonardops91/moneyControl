package br.com.challenge.moneycontrol;


import br.com.challenge.moneycontrol.DTO.IncomeDTO;
import br.com.challenge.moneycontrol.controller.IncomeController;
import br.com.challenge.moneycontrol.enumerable.IncomeCategory;
import br.com.challenge.moneycontrol.form.IncomeForm;
import br.com.challenge.moneycontrol.repository.IncomeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		IncomeController.class
})
public class IncomeTest {
	Pageable pagination = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

	//URL base para acesso
	private final String BASE_URL = "/receitas";

	//Instância do ObjectMapper para trabalhar com JSON
	private ObjectMapper objectMapper;

	//Controller REST tratado por meio de injeção de dependências
	@Autowired
	private IncomeController incomeController;

	//Instância do MockMVC
	private MockMvc mockMvc;

	//Instância do mock repositório
	@MockBean
	private IncomeRepository incomeRepository;


	@Before
	public void setUp(){
		objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		mockMvc = MockMvcBuilders
				.standaloneSetup(incomeController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void deveRetornarTodasAsReceitasDoBancoComPaginacao() throws Exception{
		IncomeForm form = new IncomeForm();
		form.setDescription("Teste");
		form.setValue(600);
		form.setDate(LocalDate.of(2022, 11, 15));
		form.setType(Type.Fixa);
		form.setCategory(IncomeCategory.Dividendos);
		Income income = form.convert();
		form.setDescription("teste 2");
		form.setValue(800);
		Income income1 = form.convert();

		List<Income> list = new ArrayList<>();
		list.add(income);
		list.add(income1);
		Page<Income> incomes = new PageImpl<>(list, pagination, 2);

		Mockito.when(incomeRepository.findAll(pagination)).thenReturn(incomes);


		mockMvc.perform(get(BASE_URL))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.numberOfElements", is(2)))
				.andExpect(jsonPath("$.totalElements", is(2)));
	}


	@Test
	public void deveRetornarValorDaReceitaPeloId() throws Exception {
		Income income = new Income("Teste", 210, LocalDate.of(2022, 12, 1),
				Type.Fixa,
				IncomeCategory.Dividendos);

				when(incomeRepository.findById(1)).thenReturn(Optional.of(income));

		mockMvc.perform(get(BASE_URL + "/1"))
				.andExpect(content()
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.description", is("Teste")))
				.andExpect(jsonPath("$.value", is(210.0)));

		verify(incomeRepository, times(1)).findById(1);
	}

	@Test
	public void deveInserirObjetoComSucessoNoBD() throws Exception {
		IncomeForm form = new IncomeForm();
		form.setDescription("Teste");
		form.setValue(550);
		form.setDate(LocalDate.of(2022, 11, 15));
		form.setType(Type.Fixa);
		form.setCategory(IncomeCategory.Dividendos);
		Income income = form.convert();

		when(incomeRepository.save(any(Income.class))).thenReturn(income);

		mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(form)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.description", is("Teste")))
				.andExpect(jsonPath("$.value", is(550.0)))
				.andExpect(jsonPath("$.category", is("Dividendos")));
//		verify(incomeRepository).save(income);
	}

	@Test
	public void deveAtualizarRegistro() throws Exception {
		IncomeForm form = new IncomeForm();
		form.setDescription("Teste");
		form.setValue(600);
		form.setDate(LocalDate.of(2022, 11, 15));
		form.setType(Type.Fixa);
		form.setCategory(IncomeCategory.Dividendos);
		Income income = form.convert();

		when(incomeRepository.findById(1)).thenReturn(Optional.of(income));
		when(incomeRepository.getById(1)).thenReturn(income);
		when(incomeRepository.save(any(Income.class))).thenReturn(income);

		mockMvc.perform(put(BASE_URL + "/1")
				.content(objectMapper.writeValueAsString(form))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.description", is("Teste")))
				.andExpect(jsonPath("$.value", is(600.0)));
	}

	@Test
	public void deveDeletarRegistro() throws Exception {
		IncomeForm form = new IncomeForm();
		form.setDescription("Teste");
		form.setValue(700);
		form.setDate(LocalDate.of(2022, 11, 15));
		form.setType(Type.Fixa);
		form.setCategory(IncomeCategory.Dividendos);
		Income income = form.convert();

		when(incomeRepository.findById(1)).thenReturn(Optional.of(income));

		mockMvc.perform(delete(BASE_URL + "/1"))
				.andExpect(status().isOk());

		verify(incomeRepository, times(1)).deleteById(1);
	}


}
