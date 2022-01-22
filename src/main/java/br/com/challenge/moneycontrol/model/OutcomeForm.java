package br.com.challenge.moneycontrol.model;

import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.repository.OutcomeRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

public class OutcomeForm {
    @NotNull @NotEmpty
    private String description;
    @NotNull @NotEmpty
    private double value;
    @NotNull @NotEmpty
    private LocalDate date;
    @NotNull @NotEmpty
    private Type type;

    public OutcomeForm(String description, double value, LocalDate date, Type type) {
        this.description = description;
        this.value = value;
        this.date = date;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Outcome convert() {
        return new Outcome(description, value, date, type);
    }

    public Outcome update(int id, OutcomeRepository outcomeRepository) {
        Outcome outcome = outcomeRepository.getById(id);
        outcome.setDescription(description);
        outcome.setValue(value);
        outcome.setDate(date);
        outcome.setType(type);
        return outcome;
    }
}
