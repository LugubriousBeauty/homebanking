package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import de.vinado.spring.mail.javamail.JavaMailSenderDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ClientService {

    public List<Client> getAllClients();

    public List<ClientDTO> getAllClientsDTO(List<Client> clients);

    public Client getClient(Long id);

    public ClientDTO getClientDTO(Client client);

    public Client findByEmail(String email);

    public void save(Client client);

    public void register(Client client, String siteURL) throws UnsupportedEncodingException, MessagingException;

    private void sendVerificationEmail(Client client, String siteURL) {}

}
