package br.com.challenge.moneycontrol.controller;

import br.com.challenge.moneycontrol.DTO.IncomeDTO;
import br.com.challenge.moneycontrol.form.IncomeForm;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.model.UserAccount;
import br.com.challenge.moneycontrol.repository.IncomeRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.util.converter.LocalDateStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/receitas")
public class IncomeController {
    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserController userController;


    @GetMapping
    @Cacheable(value = "ListaDeIncomes")
    public Page<IncomeDTO> list(String descricao, @PageableDefault(sort = "id",
            direction =
                    Sort.Direction.ASC, page = 0, size = 10) Pageable pagination) {
        Page<IncomeDTO> incomesDTO;
        Page<Income> incomes;
        if (descricao == null) {
            incomes =
                    incomeRepository.findByUser(userController.currentUser(),
                            pagination);
        } else {
            incomes =
                    incomeRepository.findByUserAndDescriptionIgnoreCaseContaining(userController.currentUser(), descricao,
                            pagination);
        }
        incomesDTO = IncomeDTO.convert(incomes);
        return incomesDTO;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO> getIncome(@PathVariable int id) {
        Optional<Income> income = incomeRepository.findById(id);
        if (income.isPresent()
                && income.get().getUser().equals(userController.currentUser())) {
            return ResponseEntity.ok(new IncomeDTO(income.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{year}/{month}")
    public Page<IncomeDTO> getIncomeByDate(
            @PathVariable int year,
            @PathVariable int month,
            @PageableDefault(
                    sort = "date",
                    direction = Sort.Direction.ASC,
                    page = 0,
                    size = 10
            ) Pageable pagination
    ) {
        LocalDate initialDate = LocalDate.of(year, month, 1);
        LocalDate finalDate = LocalDate.of(year, month, initialDate.lengthOfMonth());
        Page<Income> incomes =
                incomeRepository.findByUserAndDateBetween(
                        userController.currentUser(),
                        initialDate,
                        finalDate,
                        pagination);
        return IncomeDTO.convert(incomes);
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(value = "ListOfIncomes", allEntries = true)
    public ResponseEntity<IncomeDTO> save(@RequestBody @Valid IncomeForm incomeForm,
                                          UriComponentsBuilder uriBuilder) {
        Income income = incomeForm.convert();
        income.setUser(userController.currentUser());
        incomeRepository.save(income);
        URI uri =
                uriBuilder.path("/income/{id}").buildAndExpand(income.getId()).toUri();
        return ResponseEntity.created(uri).body(new IncomeDTO(income));

    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "ListaDeIncomes", allEntries = true)
    public ResponseEntity<IncomeDTO> update(@PathVariable int id,
                                            @RequestBody @Valid IncomeForm form) {
        Optional<Income> incomeOptional = incomeRepository.findById(id);

        if (incomeOptional.isPresent()
                && incomeOptional.get().getUser().equals(userController.currentUser())) {
            Income income = form.update(id, incomeRepository);
            return ResponseEntity.ok(new IncomeDTO(income));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable int id,
                                    @AuthenticationPrincipal UserAccount loggedUser) {
        Optional<Income> incomeOptional = incomeRepository.findById(id);
        if (incomeOptional.isPresent() &&
                incomeOptional.get().getUser().equals(userController.currentUser())) {
            incomeRepository.deleteById(id);
            return ResponseEntity.ok().body("Receita removida com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }
}
