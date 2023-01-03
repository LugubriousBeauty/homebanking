package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;


    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionRepository
                .findAll()
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @RequestMapping(path = "/clients/current/transactions", method = RequestMethod.POST)
    ResponseEntity<Object> register(Authentication authentication, @RequestParam Double amount,
                                    @RequestParam String description, @RequestParam String originNumber,
                                    @RequestParam String destinyNumber) {

        if(amount.isNaN() || description.isEmpty() || originNumber.isEmpty() || destinyNumber.isEmpty()) {
            return new ResponseEntity<>("Fields cannot be empty", HttpStatus.FORBIDDEN);
        }

        if(originNumber == destinyNumber) {
            return new ResponseEntity<>("accounts cannot be the same", HttpStatus.FORBIDDEN);
        }

        Account originAccount = accountRepository.findByNumber(originNumber);
        Account destinyAccount = accountRepository.findByNumber(destinyNumber);

        if(originAccount == null ||destinyAccount == null) {
            return new ResponseEntity<>("Account/s not found", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());

        if(client == null) {
            return new ResponseEntity<>("user doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(originAccount)) {
            return new ResponseEntity<>("origin account doesnt belong to the authenticated user", HttpStatus.FORBIDDEN);
        }

        if(originAccount.getBalance() < amount) {
            return new ResponseEntity<>("not enough founds", HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now());

        debitTransaction.setDescription(description + " " + destinyNumber);
        creditTransaction.setDescription(description + " " + originNumber);

        originAccount.addTransaction(debitTransaction);
        originAccount.setBalance(originAccount.getBalance() - amount);

        destinyAccount.addTransaction(creditTransaction);
        destinyAccount.setBalance(destinyAccount.getBalance() + amount);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        accountRepository.save(originAccount);
        accountRepository.save(destinyAccount);

        return new ResponseEntity<>("created", HttpStatus.CREATED);
    }
}
