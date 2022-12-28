package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @RequestMapping("/cards/{id}")
    public CardDTO getCards(@PathVariable Long id) {
        return cardRepository
                .findById(id)
                .map(CardDTO::new)
                .orElse(null);
    }
    @RequestMapping("/cards")
    public List<CardDTO> getCards() {
        return cardRepository
                .findAll()
                .stream()
                .map(CardDTO::new)
                .collect(toList());
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> register (
            @RequestParam CardColor color, @RequestParam CardType type,
            Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if(client.getCards().stream().filter(card -> card.getColor() == color).collect(Collectors.toSet()).size() > 2) {
            return new ResponseEntity<>("Cards limit exceeded", HttpStatus.FORBIDDEN);
        }

        if (color.toString().isEmpty() || type.toString().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        String number = getRandomNumber(0000,9999) + "-" + getRandomNumber(0000,9999) + "-" +
                getRandomNumber(0000,9999) + "-" + getRandomNumber(0000,9999);
        Card card = new Card(client.getFirstName() + " " + client.getLastName(), number, getRandomNumber(111,999),
                LocalDate.now(), LocalDate.now().plusYears(5), type, color);
        cardRepository.save(card);
        client.addCard(card);
        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
