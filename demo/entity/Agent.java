package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent-id" ,nullable = false)
    private Long id;
    @JsonProperty("nom")
    private String nom;
    @JsonProperty("prenom")
    private  String prenom;
    private  PieceIdentite pieceIdentite;
    private Long numeroIdentite;
    private String adresse;
    private String email;
    private String numeroTelephone;
    private String immatriculation;
    private String description;
    private String patente;
    private LocalDate dateofbirth;
    private boolean isValidated=false;
    private String otp;
    private LocalDateTime otpCreatedAt;
    private LocalDateTime createdAt;
    private String nomFichier;
    private int number_of_client=0;
    @Column(unique = false)
    private  String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private boolean firstLogin;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_agence")
    private Agence agence;
    @OneToMany(mappedBy = "agent" ,cascade =CascadeType.ALL )
    private List<Client> clients;

    public void setOtp(String otp) {
        this.otp = otp;
        this.otpCreatedAt = LocalDateTime.now();
    }
    public boolean validateOtp(String otp) {
        if (this.otp.equals(otp)) {
            // Vérifier si l'OTP est toujours valide (par exemple 5 minutes)
            if (this.otpCreatedAt.isAfter(LocalDateTime.now().minusMinutes(5))) {
                return true;
            }
        }
        return false;
    }


}
