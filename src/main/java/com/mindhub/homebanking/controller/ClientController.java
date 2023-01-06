package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientService clientService;

    @PostMapping("/process_register")
    public String processRegister(Client client, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        clientService.register(client, getSiteURL(request));
        return "register_success";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClientDTO(clientService.getClient(id));
    }

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
       return clientService.getAllClientsDTO(clientService.getAllClients());
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


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
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Mail already in use", HttpStatus.FORBIDDEN);
        }

        String accountNumber = getRandomNumber(0000,9999);
        Account account = new Account(accountNumber, LocalDateTime.now(), 0.00);
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        accountRepository.save(account);
        client.addAccount(account);
        clientService.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrent(Authentication authentication) {
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }
}
