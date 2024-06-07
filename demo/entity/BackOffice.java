package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BackOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backOffice_id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String role="ROLE_ADMIN";
    private String username;
    @OneToMany(cascade = CascadeType.ALL,
    fetch = FetchType.EAGER)
    @JoinColumn(name = "backOffice_id")
    private List<Agent> agents=new ArrayList<>();


    public BackOffice(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
