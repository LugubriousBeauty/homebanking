package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImp implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    @Override
    public List<ClientDTO> getAllClientsDTO(List<Client> clients) {
        return clients
                .stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }
    @Override
    public Client getClient(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if(client.isPresent()) {
            return client.get();
        } else {
            return null;
        }
    }
    @Override
    public ClientDTO getClientDTO(Client client) {
        if(client == null) {
            return null;
        } else {
            return new ClientDTO(client);
        }
    }
    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }
    @Override
    public void sendVerificationEmail(Client client, String siteURL) throws MessagingException, UnsupportedEncodingException{
        String subject = "Please verify your registration";
        String senderName = "Space Bank";
        String mailContent = "<p>Dear " + client.getFirstName() + ",</p>";
        mailContent += "<p>Please click the link below to verify your registration: </p>";
        mailContent += "<p>Thank you <br> Space Bank Team</p>";

        String verifyURL = siteURL + "/verify?code=" + client.getVerificationCode();
        mailContent += "<a = \"href =" + siteURL + "\">VERIFY</a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("lucividal09@gmail.com", senderName);
        helper.setTo(client.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void createClient(Client client, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String siteURL = request.getRequestURL().toString();
        sendVerificationEmail(client, siteURL);
    }
}
