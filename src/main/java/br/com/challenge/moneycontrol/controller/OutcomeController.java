package br.com.challenge.moneycontrol.controller;

import br.com.challenge.moneycontrol.DTO.OutcomeDTO;
import br.com.challenge.moneycontrol.model.Outcome;
import br.com.challenge.moneycontrol.model.OutcomeForm;
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
import java.util.Optional;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/despesas")
public class OutcomeController {
    @Autowired
    OutcomeRepository outcomeRepository;

    @GetMapping
    @Cacheable(value = "listOfOutcomes")
    public Page<OutcomeDTO> list(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC,
                    page = 0, size = 10) Pageable pagination
    ) {
        Page<Outcome> outcomes = outcomeRepository.findAll(pagination);
        Page<OutcomeDTO> outcomesDTO = OutcomeDTO.convert(outcomes);
        return outcomesDTO;
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "ListOfOutcomes", allEntries = true)
    public ResponseEntity<?> save(@RequestBody @Valid OutcomeForm form,
                                  UriComponentsBuilder uriBuilder) {
        Outcome outcome = form.convert();
        outcomeRepository.save(outcome);
        URI uri =
                uriBuilder.path("/outcomes/{id}").buildAndExpand(outcome.getId()).toUri();
        return ResponseEntity.created(uri).body(new OutcomeDTO(outcome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutcomeDTO> getOutcome(@PathVariable int id){
        Optional<Outcome> outcomeOptional = outcomeRepository.findById(id);
        if(outcomeOptional.isPresent()){
            return ResponseEntity.ok(new OutcomeDTO(outcomeOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listOfOutcomes")
    public ResponseEntity<OutcomeDTO> update(@PathVariable int id,
            @RequestBody @Valid OutcomeForm form){
        Optional<Outcome> outcomeOptional = outcomeRepository.findById(id);
        if (outcomeOptional.isPresent()){
            Outcome outcome = form.update(id, outcomeRepository);
            return ResponseEntity.ok(new OutcomeDTO(outcome));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable int id){
        Optional<Outcome> outcomeOptional = outcomeRepository.findById(id);
        if(outcomeOptional.isPresent()){
            outcomeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
