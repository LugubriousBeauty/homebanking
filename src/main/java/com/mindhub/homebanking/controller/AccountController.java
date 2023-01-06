package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

//Escucha y responde las peticiones (requests) del cliente. Maneja las direcciones de las URL

//MC -> Arquitectura modelo controlador -> "devuelve" html
//Rest controller -> devuelve JSON o XML

@RestController
@RequestMapping("/api") /**/
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountRepository
                .findById(id)
                .map(AccountDTO::new)
                .orElse(null);
    }
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository
                .findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(toList());
    }

    public String getRandomNumber(int min, int max) {
        Random random = new Random();
        String number = "VIN" + random.nextInt(max - min) + min;
        String finalNumber = number;
        Set<String> accountsNumbers = accountRepository.findAll().stream().map(account -> account.getNumber())
                .filter(accountNumber -> accountNumber == finalNumber).collect(Collectors.toSet());
        if(accountsNumbers.size() > 0) {
            number = getRandomNumber(0000, 9999);
        }
        return number;
    }


    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> register(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client.getAccounts().size() > 2) {
            return new ResponseEntity<>("Accounts limit exceeded", HttpStatus.FORBIDDEN);
        }
        String accountNumber = getRandomNumber(0000,9999);
        Account account = new Account(accountNumber, LocalDateTime.now(), 0.00);
        accountRepository.save(account);
        client.addAccount(account);
        clientRepository.save(client);
        return new ResponseEntity<>("created ",HttpStatus.CREATED);
    }


}
