package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			Client lucia = new Client("Lucia", "Vidal", "lucividal09@gmail.com");
			Client dante = new Client("Dante", "Vilches", "dantevilches@gmail.com");
			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.00);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 10000.00);
			Account account4 = new Account("VIN004", LocalDateTime.now().plusDays(1), 2000.00);
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 150.00, "pruebaa", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 150.00, "pruebaa2", LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 1200.00, "pruebaa3", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 1400.00, "pruebaa4", LocalDateTime.now());
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, 14500.00, "pruebaa4", LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.CREDIT, 14003.00, "pruebaa4", LocalDateTime.now());
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction5);
			account1.addTransaction(transaction6);
			account2.addTransaction(transaction2);
			account3.addTransaction(transaction3);
			account4.addTransaction(transaction4);
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
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

		};
	}
}
