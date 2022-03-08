package br.com.challenge.moneycontrol.controller;

import br.com.challenge.moneycontrol.DTO.OutcomeDTO;
import br.com.challenge.moneycontrol.model.Outcome;
import br.com.challenge.moneycontrol.form.OutcomeForm;
import br.com.challenge.moneycontrol.repository.OutcomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/despesas")
public class OutcomeController {
    @Autowired
    private OutcomeRepository outcomeRepository;

    @Autowired
    private UserController userController;

    @GetMapping
    @Cacheable(value = "listOfOutcomes")
    public Page<OutcomeDTO> list(String descricao, @PageableDefault(sort = "id", direction = Sort.Direction.ASC,
                                         page = 0, size = 10) Pageable pagination
    ) {
        Page<OutcomeDTO> outcomesDTO;
        Page<Outcome> outcomes;
        if (descricao == null) {
            outcomes = outcomeRepository.findByUser(pagination,
                    userController.currentUser());
        } else {
            outcomes =
                    outcomeRepository.findByUserAndDescriptionIgnoreCaseContaining(descricao,
                            pagination, userController.currentUser());
        }
        outcomesDTO = OutcomeDTO.convert(outcomes);
        return outcomesDTO;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutcomeDTO> getOutcome(@PathVariable int id) {
        Optional<Outcome> outcome = outcomeRepository.findById(id);
        if (outcome.isPresent() && outcome.get().getUser().equals(userController.currentUser())) {
            return ResponseEntity.ok(new OutcomeDTO(outcome.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{year}/{month}")
    public Page<OutcomeDTO> getOutcomesByDate(
            @PathVariable int year,
            @PathVariable int month,
            @PageableDefault(
                    sort = "date",
                    direction = Sort.Direction.ASC,
                    page = 0, size = 10
            ) Pageable pagination
    ) {
        LocalDate initialDate = LocalDate.of(year, month, 1);
        LocalDate finalDate = LocalDate.of(year, month, initialDate.lengthOfMonth());
        Page<Outcome> outcomes =
                outcomeRepository.findByUserAndDateBetween(
                        userController.currentUser(),
                        initialDate,
                        finalDate,
                        pagination);
        return OutcomeDTO.convert(outcomes);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listOfOutcomes", allEntries = true)
    public ResponseEntity<?> save(@RequestBody @Valid OutcomeForm form,
                                  UriComponentsBuilder uriBuilder) {
        Outcome outcome = form.convert();
        outcome.setUser(userController.currentUser());
        outcomeRepository.save(outcome);
        URI uri =
                uriBuilder.path("/outcomes/{id}").buildAndExpand(outcome.getId()).toUri();
        return ResponseEntity.created(uri).body(new OutcomeDTO(outcome));
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listOfOutcomes")
    public ResponseEntity<OutcomeDTO> update(@PathVariable int id,
                                             @RequestBody @Valid OutcomeForm form) {
        Optional<Outcome> outcomeOptional = outcomeRepository.findById(id);
        if (outcomeOptional.isPresent() && outcomeOptional.get().getUser().equals(userController.currentUser())) {
            Outcome outcome = form.update(id, outcomeRepository);
            return ResponseEntity.ok(new OutcomeDTO(outcome));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable int id) {
        Optional<Outcome> outcomeOptional = outcomeRepository.findById(id);
        if (outcomeOptional.isPresent() && outcomeOptional.get().getUser().equals(userController.currentUser())) {
            outcomeRepository.deleteById(id);
            return ResponseEntity.ok().body("Despesa removida com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

}
