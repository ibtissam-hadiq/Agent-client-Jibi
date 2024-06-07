package com.example.demo.controller;

import com.example.demo.dto.OtpValidationRequest;
import com.example.demo.entity.Agent;
import com.example.demo.entity.BackOffice;
import com.example.demo.service.BackOfficeService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/backoffice")
public class BackofficeController {
    @Autowired
    private BackOfficeService backOfficeService;
    @PostMapping("/addAgent")
    public ResponseEntity<?> addAgent(@ModelAttribute Agent newAgent, @RequestPart("cinPdfFile") MultipartFile cinPdfFile) throws Exception {
//        backOfficeService.addAgent(newAgent);
//        return ResponseEntity.ok().build();
        try {
            backOfficeService.addAgent(newAgent, cinPdfFile);
            return ResponseEntity.ok("Agent added successfully");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error adding agent: " + e.getMessage());
        }
    }

    @GetMapping("/getBackOffice/{username}")
    public ResponseEntity<BackOffice> getBackOffice(@PathVariable String username) {
        BackOffice backOffice = backOfficeService.getBackOffice(username);
        return ResponseEntity.ok(backOffice);
    }

    @PostMapping("/saveAdmin")
    public ResponseEntity<BackOffice> saveAdmin(@RequestBody BackOffice backOffice) {
        BackOffice savedBackOffice = backOfficeService.saveAdmin(backOffice);
        return ResponseEntity.ok(savedBackOffice);
    }
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        try {
            backOfficeService.sendOtpForPasswordReset(email);
            return ResponseEntity.ok("OTP envoyé avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
        boolean isValid = backOfficeService.validateOtp(otpValidationRequest.getUsername(), otpValidationRequest.getOtpNumber());
        if (isValid) {
            return ResponseEntity.ok("OTP validé avec succès");
        } else {
            return ResponseEntity.status(400).body("OTP invalide");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String email, @RequestParam String newPassword) {
        try {
            backOfficeService.changePassword(email, newPassword);
            return ResponseEntity.ok("Mot de passe changé avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
