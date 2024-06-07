package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String cin;
    private String phoneNumber="+212 687157227";
    private String otp;
    private LocalDateTime otpExpiry;
    private String Username;
    private String password;
    @OneToOne(mappedBy = "client")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;
}
