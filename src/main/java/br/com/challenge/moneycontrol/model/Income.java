package br.com.challenge.moneycontrol.model;

import java.time.LocalDate;

import javax.persistence.*;

import br.com.challenge.moneycontrol.enumerable.IncomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;


@Entity
public class Income {
	@Id @GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;
	private String description;
	private double value;
	private LocalDate date;
	@Enumerated(EnumType.STRING)
	private Type type;
	@Enumerated(EnumType.STRING)
	private IncomeCategory category;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserAccount user;

	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}

	public Income(){}
	public Income(String description, double value, LocalDate date, Type type
			, IncomeCategory category) {
		super();
		this.description = description;
		this.value = value;
		this.date = date;
		this.type = type;
		this.category = category;
	}
	public Income(String description, double value, LocalDate date, Type type
			, IncomeCategory category, UserAccount user) {
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
	public IncomeCategory getCategory() {
		return category;
	}
	public void setCategory(IncomeCategory category) {
		this.category = category;
	}
}
