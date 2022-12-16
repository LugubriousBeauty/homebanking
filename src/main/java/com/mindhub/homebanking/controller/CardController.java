package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    CardRepository cardRepository;

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

}
