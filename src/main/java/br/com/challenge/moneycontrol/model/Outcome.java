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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

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

    public Outcome(String description, double value, LocalDate date,
                   Type type, OutcomeCategory category, UserAccount user) {
        super();
        this.description = description;
        this.value = value;
        this.date = date;
        this.type = type;
        this.category = category;
        this.user = user;
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

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }
}
