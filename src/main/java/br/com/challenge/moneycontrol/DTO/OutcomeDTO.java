package br.com.challenge.moneycontrol.DTO;

import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.model.Outcome;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public class OutcomeDTO {
    private String description;
    private double value;
    private LocalDate date;
    private Type type;
    private OutcomeCategory category;

    public OutcomeDTO(Outcome outcome) {
        this.description = outcome.getDescription();
        this.value = outcome.getValue();
        this.date = outcome.getDate();
        this.type = outcome.getType();
        this.category = outcome.getCategory();
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

    public OutcomeCategory getCategory() {
        return category;
    }

    public void setCategory(OutcomeCategory category) {
        this.category = category;
    }

    public static Page<OutcomeDTO> convert(Page<Outcome> outcomes){
        return outcomes.map(OutcomeDTO::new);
    }
}
