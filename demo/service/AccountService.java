package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Client;
import com.example.demo.repo.AccountRepository;
import com.example.demo.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository ;
    public Account createAccount(Long id, Float initialBalance) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        Account account = new Account();
        account.setClient(client);
        account.setBalance(initialBalance);
        return accountRepository.save(account);
    }

    public boolean checkBalance(Long accountId, Float amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getBalance() >= amount;
    }

    public void debitBalance(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance((float) (account.getBalance() - amount));
        accountRepository.save(account);
    }
    public void creditBalance(Long accountId, Float amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
    
}
