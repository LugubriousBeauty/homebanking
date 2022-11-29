package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			Client lucia = new Client("Lucia", "Vidal", "lucividal09@gmail.com");
			Client dante = new Client("Dante", "Vilches", "dantevilches@gmail.com");
			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.00);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 10000.00);
			Account account4 = new Account("VIN004", LocalDateTime.now().plusDays(1), 2000.00);
			lucia.addAccount(account1);
			lucia.addAccount(account2);
			dante.addAccount(account3);
			dante.addAccount(account4);
			clientRepository.save(lucia);
			clientRepository.save(dante);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

		};
	}
}
