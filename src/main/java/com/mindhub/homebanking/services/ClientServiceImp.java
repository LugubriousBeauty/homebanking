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

    public void register(Client client, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(client.getPassword());
        client.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        client.setVerificationCode(randomCode);
        client.setEnabled(false);

        clientRepository.save(client);

        sendVerificationEmail(client, siteURL);
    }

    private void sendVerificationEmail(Client client, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = client.getEmail();
        String fromAddress = "Your email address";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", client.getFirstName() + " " + client.getLastName());
        String verifyURL = siteURL + "/verify?code=" + client.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }
}
