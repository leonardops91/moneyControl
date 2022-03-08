package br.com.challenge.moneycontrol.form;

import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Outcome;
import br.com.challenge.moneycontrol.repository.OutcomeRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class OutcomeForm {
    @NotNull @NotEmpty
    private String description;
    @NotNull
    private double value;
    @NotNull
    private LocalDate date;
    @NotNull
    private Type type;
    private OutcomeCategory category = OutcomeCategory.Outras;


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

    public OutcomeCategory getCategory() {
        return category;
    }

    public void setCategory(OutcomeCategory category) {
        this.category = category;
    }

    public Outcome convert() {
        return new Outcome(description, value, date, type, category);
    }

    public Outcome update(int id, OutcomeRepository outcomeRepository) {
        Outcome outcome = outcomeRepository.getById(id);
        outcome.setDescription(description);
        outcome.setValue(value);
        outcome.setDate(date);
        outcome.setType(type);
        outcome.setCategory(category);
        return outcome;
    }
}
