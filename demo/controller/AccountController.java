package com.example.demo.controller;

import com.example.demo.dto.OtpRequest;
import com.example.demo.dto.OtpValidationRequest;
import com.example.demo.entity.Account;
import com.example.demo.entity.Client;
import com.example.demo.service.AccountService;
import com.example.demo.service.ClientService;
import com.example.demo.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private SmsService smsService;

    @PostMapping("/addAccount")
    public Account addAccount(@RequestParam Long id, @RequestParam Float initialBalance) {
        return accountService.createAccount(id, initialBalance);
    }
    @PostMapping("/sendClientOtp")
    public void sendClientOtp(@RequestParam Long clientId) {
        Client client = clientService.findById(clientId);
        if (client != null) {
            OtpRequest otpRequest = new OtpRequest(client.getCin(), client.getPhoneNumber());
            smsService.sendOtpToClient(otpRequest);
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    @PostMapping("/creditBalanceWithOtp")
    public void creditBalanceWithOtp(@RequestParam Long clientId, @RequestParam Float amount, @RequestParam String otp) {
        Client client = clientService.findById(clientId);
        if (client != null && smsService.validateClientOtp(new OtpValidationRequest(client.getCin(), otp)).equals("OTP is valid!")) {
            accountService.creditBalance(client.getAccount().getId(), amount);
        } else {
            throw new RuntimeException("Invalid OTP or Client not found");
        }
    }

}
