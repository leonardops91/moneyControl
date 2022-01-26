package br.com.challenge.moneycontrol.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import br.com.challenge.moneycontrol.DTO.OutcomeDTO;
import br.com.challenge.moneycontrol.controller.OutcomeController;
import br.com.challenge.moneycontrol.enumerable.OutcomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Entity
public class Outcome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private double value;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private OutcomeCategory category;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + id;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Outcome other = (Outcome) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id != other.id)
            return false;
        if (type != other.type)
            return false;
        if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
            return false;
        return true;
    }

    public Outcome() {
    }

    public Outcome(String description, double value, LocalDate date,
                   Type type, OutcomeCategory category) {
        super();
        this.description = description;
        this.value = value;
        this.date = date;
        this.type = type;
        this.category = category;
    }

    public int getId() {
        return id;
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

    public static double totalOutcome(int year, int month, Pageable pageable) {
        OutcomeController controller = new OutcomeController();
        double soma;
        Page<OutcomeDTO> outcomes = controller.getOutcomesByDate(year, month,
                pageable);
        System.out.println(outcomes);
        soma = outcomes.stream().mapToDouble(OutcomeDTO::getValue).sum();
        System.out.println("entrei");
        return soma;
    }
}
