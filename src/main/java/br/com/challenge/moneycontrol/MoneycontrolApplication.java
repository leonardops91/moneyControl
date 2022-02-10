package br.com.challenge.moneycontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport
public class MoneycontrolApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneycontrolApplication.class, args);
	}

}
