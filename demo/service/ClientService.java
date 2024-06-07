package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository ;
    @Autowired
    private BackOfficeService backOfficeService;
    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
    }
    public Client createClient(Client client) {
        client.setFirstName(client.getFirstName());
        client.setLastName(client.getLastName());
        client.setCin(client.getCin());
        client.setUsername(backOfficeService.generateUsername(client.getFirstName(), client.getLastName()));
        client.setPassword("1234");

        return clientRepository.save(client);
    }
}
