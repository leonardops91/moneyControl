package br.com.challenge.moneycontrol.form;

import br.com.challenge.moneycontrol.enumerable.IncomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;
import br.com.challenge.moneycontrol.repository.IncomeRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;


public class IncomeForm {
    @NotNull @NotEmpty
    private String description;
    @NotNull
    private double value;
    @NotNull
    private LocalDate date;
    @NotNull
    private Type type;
    private IncomeCategory category = IncomeCategory.Outras;

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

    public IncomeCategory getCategory() {
        return category;
    }

    public void setCategory(IncomeCategory category) {
        this.category = category;
    }

    public Income convert(){
        return new Income(description, value, date, type, category);
    }

    public Income update(int id, IncomeRepository incomeRepository) {
        Income income = incomeRepository.getById(id);
        income.setDescription(this.description);
        income.setValue(this.value);
        income.setDate(this.date);
        income.setType(this.type);
        income.setCategory(category);
        return income;
    }
}
