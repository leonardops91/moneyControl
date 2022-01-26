package br.com.challenge.moneycontrol;

import br.com.challenge.moneycontrol.controller.ResumeController;
import org.junit.Test;

import java.time.LocalDate;

public class ResumeControllerTest {

    @Test
    public void deveriaRetornarListaDeCategorias() {
        ResumeController resumeController = new ResumeController();
        resumeController.setOutcomeByCategory(LocalDate.of(2022
                , 05, 01), LocalDate.of(2022
                        , 05, 31));
    }
}
