package br.com.challenge.moneycontrol.DTO;

import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IncomeDTO {
    private int id;
    private String description;
    private double value;
    private LocalDate date;
    private Type type;

    public IncomeDTO(Income income) {
        this.id = income.getId();
        this.description = income.getDescription();
        this.value = income.getValue();
        this.date = income.getDate();
        this.type = income.getType();
    }


    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date;
    }

//    public static List<IncomeDTO> convert(List<Income> incomes) {
//        List<IncomeDTO> incomesDTO = new ArrayList<>();
//        incomes.forEach(income -> {
//            incomesDTO.add(new IncomeDTO(income));
//        });
//        return incomesDTO;
//    }
    public static Page<IncomeDTO> convert(Page<Income> incomes){
        return incomes.map(IncomeDTO::new);
    }
}
