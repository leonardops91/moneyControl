package br.com.challenge.moneycontrol.controller;

import br.com.challenge.moneycontrol.DTO.IncomeDTO;
import br.com.challenge.moneycontrol.form.IncomeForm;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.repository.IncomeRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/receitas")
public class IncomeController {
    @Autowired
    private IncomeRepository incomeRepository;

    @GetMapping
    @Cacheable(value = "ListaDeIncomes")
    public Page<IncomeDTO> list(@PageableDefault(sort = "id", direction =
            Sort.Direction.ASC, page = 0, size = 10) Pageable pagination) {
        Page<Income> incomes = incomeRepository.findAll(pagination);
        Page<IncomeDTO> incomesDTO = IncomeDTO.convert(incomes);
        return incomesDTO;
    }

//    @GetMapping
//    @Cacheable(value = "ListaDeIncomes")
//    public List<IncomeDTO> list(@PageableDefault(sort = "id", direction =
//            Sort.Direction.DESC, page = 0, size = 10) Pageable paginacao) {
//        List<Income> incomes = incomeRepository.findAll();
//        List<IncomeDTO> incomesDTO = IncomeDTO.convert(incomes);
//        incomesDTO.forEach(incomeDTO -> {
//            System.out.println(incomeDTO.getDescription());
//        });
//        return incomesDTO;
//    }
        @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Transactional
    @CacheEvict(value = "ListaDeIncomes", allEntries = true)
    public ResponseEntity<IncomeDTO> save(@RequestBody @Valid IncomeForm incomeForm,
                                          UriComponentsBuilder uriBuilder){
        Income income = incomeForm.convert();
        incomeRepository.save(income);
        URI uri =
                uriBuilder.path("/income/{id}").buildAndExpand(income.getId()).toUri();
        return ResponseEntity.created(uri).body(new IncomeDTO(income));
    }
//    @PostMapping(
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @CacheEvict(value = "ListaDeIncomes", allEntries = true)
//    public String save(@RequestBody @Valid IncomeForm incomeForm,
//                       UriComponentsBuilder uriBuilder) {
//        Income income = incomeForm.convert();
//        incomeRepository.save(income);
//        URI uri =
//                uriBuilder.path("/income/{id}").buildAndExpand(income.getId()).toUri();
//        return "Ok, salvo com sucesso!";
//    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO> getIncome(@PathVariable int id) {
        Optional<Income> income = incomeRepository.findById(id);
        if(income.isPresent()){
            return ResponseEntity.ok(new IncomeDTO(income.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "ListaDeIncomes", allEntries = true)
    public ResponseEntity<IncomeDTO> update(@PathVariable int id,
                                            @RequestBody @Valid IncomeForm form){
        Optional<Income> incomeOptional = incomeRepository.findById(id);
        if(incomeOptional.isPresent()){
            Income income = form.update(id, incomeRepository);
            return ResponseEntity.ok(new IncomeDTO(income));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable int id){
        Optional<Income> incomeOptional = incomeRepository.findById(id);
        if(incomeOptional.isPresent()){
            incomeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}