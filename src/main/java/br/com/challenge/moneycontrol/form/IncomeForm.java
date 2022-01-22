package br.com.challenge.moneycontrol.form;

import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.repository.IncomeRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

public class IncomeForm {
    @NotNull @NotEmpty
    private String description;
    @NotNull @NotEmpty
    private double value;
    @NotNull @NotEmpty
    private LocalDate date;
    @NotNull @NotEmpty
    private Type type;

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

    public Income convert(){
        return new Income(description, value, date, type);
    }

    public Income update(int id, IncomeRepository incomeRepository) {
        Income income = incomeRepository.getById(id);
        income.setDescription(this.description);
        income.setValue(this.value);
        income.setDate(this.date);
        income.setType(this.type);
        return income;
    }
}
