package com.example.demo.service;

import com.example.demo.config.TwilioConfig;
import com.example.demo.dto.OtpRequest;
import com.example.demo.dto.OtpResponseDto;
import com.example.demo.dto.OtpStatus;
import com.example.demo.dto.OtpValidationRequest;
import com.example.demo.entity.Agent;
import com.example.demo.entity.Client;
import com.example.demo.repo.AgentRepository;
import com.example.demo.repo.ClientRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.rest.lookups.v1.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;


import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class SmsService {
    @Autowired
    private TwilioConfig twilioConfig;
    @Autowired
      private AgentRepository agentRepository;
    @Autowired
       private ClientRepository clientRepository;
    Map<String, String> otpMap = new HashMap<>();
    public OtpResponseDto sendSMS(OtpRequest otpRequest) {
        OtpResponseDto otpResponseDto = null;
        Twilio.init(twilioConfig.getAccountSid(),twilioConfig.getAuthToken());
        try {
            PhoneNumber to = new PhoneNumber(otpRequest.getPhoneNumber());//to

            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from
            String otp = generateOTP();


            String otpMessage = "Dear Customer , Your OTP is  " + otp + " for sending sms through Spring boot application. Thank You.";
            Message message = Message.creator(to, from, otpMessage).create();
            //otpMap.put(otpRequest.getUsername(), otp);//otherwise we need to store it in db
            Optional<Agent> agentOpt = agentRepository.findByUsername(otpRequest.getUsername());
            if(agentOpt.isPresent()){
                Agent agent=agentOpt.get();
                agent.setOtp(otp);
                agentRepository.save(agent);
                otpResponseDto = new OtpResponseDto(OtpStatus.DELIVERED, otpMessage);
            }else {
                otpResponseDto=new OtpResponseDto(OtpStatus.FAILED,"Agent not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            otpResponseDto = new OtpResponseDto(OtpStatus.FAILED, e.getMessage());
        }
        return otpResponseDto;
    }
    public String validateOtp(OtpValidationRequest otpValidationRequest) {
//        Set<String> keys = otpMap.keySet();
//        String username = null;
//        for(String key : keys)
//            username = key;
//        if (otpValidationRequest.getUsername().equals(username)) {
//            otpMap.remove(username,otpValidationRequest.getOtpNumber());
//            return "OTP is valid!";// we can add expiration time
//        } else {
//            return "OTP is invalid!";
//        }
        Optional<Agent> agentOpt = agentRepository.findByUsername(otpValidationRequest.getUsername());
        if (agentOpt.isPresent()) {
            Agent agent = agentOpt.get();
            if (agent.validateOtp(otpValidationRequest.getOtpNumber())) {
                return "OTP is valid!";
            }
        }
        return "OTP is invalid!";

    }

    private String generateOTP() {
        return new DecimalFormat("000000")//six digit format
                .format(new Random().nextInt(999999));
    }
    //otp for client
    public OtpResponseDto sendOtpToClient(OtpRequest otpRequest) {
        OtpResponseDto otpResponseDto = null;
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        try {
            PhoneNumber to = new PhoneNumber(otpRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());
            String otp = generateOTP();
            LocalDateTime otpExpiry = LocalDateTime.now().plusMinutes(5); // OTP valide pour 5 minutes

            String otpMessage = "Dear Customer, Your OTP is " + otp + ". It is valid for 5 minutes.";
            Message message = Message.creator(to, from, otpMessage).create();

            Optional<Client> clientOpt = clientRepository.findByCin(otpRequest.getUsername());
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                client.setOtp(otp);
                client.setOtpExpiry(otpExpiry);
                clientRepository.save(client);
                otpResponseDto = new OtpResponseDto(OtpStatus.DELIVERED, otpMessage);
            } else {
                otpResponseDto = new OtpResponseDto(OtpStatus.FAILED, "Client not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            otpResponseDto = new OtpResponseDto(OtpStatus.FAILED, e.getMessage());
        }
        return otpResponseDto;

    }
    //validate otp for client
    public String validateClientOtp(OtpValidationRequest otpValidationRequest) {
        Optional<Client> clientOpt = clientRepository.findByCin(otpValidationRequest.getUsername());
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            if (client.getOtp().equals(otpValidationRequest.getOtpNumber()) && client.getOtpExpiry().isAfter(LocalDateTime.now())) {
                return "OTP is valid!";
            } else {
                return "OTP is invalid or expired!";
            }
        }
        return "Client not found!";
    }

}


