package br.com.challenge.moneycontrol.DTO;

import br.com.challenge.moneycontrol.enumerable.IncomeCategory;
import br.com.challenge.moneycontrol.enumerable.Type;
import br.com.challenge.moneycontrol.model.Income;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public class IncomeDTO {
    private int id;
    private String description;
    private double value;
    private LocalDate date;
    private Type type;
    private IncomeCategory category;

    public IncomeDTO(Income income) {
        this.id = income.getId();
        this.description = income.getDescription();
        this.value = income.getValue();
        this.date = income.getDate();
        this.type = income.getType();
        this.category = income.getCategory();
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

    public IncomeCategory getCategory() {
        return category;
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
