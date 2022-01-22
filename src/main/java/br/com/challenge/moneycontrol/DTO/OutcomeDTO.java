package br.com.challenge.moneycontrol.DTO;

import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.model.Outcome;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class OutcomeDTO {
    @NotNull @NotEmpty
    private String description;
    @NotNull @NotEmpty
    private double value;
    @NotNull @NotEmpty
    private LocalDate date;
    @NotNull @NotEmpty
    private Type type;

    public OutcomeDTO(Outcome outcome) {
        this.description = outcome.getDescription();
        this.value = outcome.getValue();
        this.date = outcome.getDate();
        this.type = outcome.getType();
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

    public static Page<OutcomeDTO> convert(Page<Outcome> outcomes){
        return outcomes.map(OutcomeDTO::new);
    }
}
