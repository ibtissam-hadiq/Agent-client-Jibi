package com.example.demo.service;

import com.example.demo.dto.OtpRequest;
import com.example.demo.dto.OtpValidationRequest;
import com.example.demo.entity.Agent;
import com.example.demo.entity.BackOffice;
import com.example.demo.repo.AgentRepository;
import com.example.demo.repo.BackOfficeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BackOfficeService {
    @Autowired
    private BackOfficeRepository backOfficeRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private MailService mailService;
    @Autowired
    private SmsService smsService;
    private static final String UPLOAD_DIR = "C:/Users/pc/AppData/Local/Temp/tomcat.8081.758963429610034516/work/Tomcat/localhost/ROOT/myDirectory/";
    @PostConstruct
    public void init() throws IOException {
       // String upload = "C:/Users/pc/AppData/Local/Temp/tomcat.8081.758963429610034516/work/Tomcat/localhost/ROOT/myDirectory/";
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            if (!uploadDir.canWrite()) {
                System.out.println("Le répertoire d'uploads n'est pas accessible en écriture.");
            } else {
                System.out.println("Répertoire d'uploads créé et accessible en écriture.");
            }
        } else {
            System.out.println("Le répertoire d'uploads existe déjà.");
        }
    }
    public void addAgent(Agent newAgent, MultipartFile cinPdfFile) throws MessagingException, IOException {
        Agent agent =new Agent();
        agent.setNom(newAgent.getNom());
        agent.setPrenom(newAgent.getPrenom());
        agent.setAdresse(newAgent.getAdresse());
        agent.setImmatriculation(newAgent.getImmatriculation());
        agent.setPatente(newAgent.getImmatriculation());
        agent.setDateofbirth(newAgent.getDateofbirth());
        agent.setNumeroTelephone(newAgent.getNumeroTelephone());
        agent.setDescription(newAgent.getDescription());
      //  agent.setNomFichier(newAgent.getNomFichier());
        //Generate file name
        String uniqueFileName = UUID.randomUUID().toString() + "_" + cinPdfFile.getOriginalFilename();
        File dest = new File(UPLOAD_DIR + uniqueFileName);
        // Save the file locally
        cinPdfFile.transferTo(dest);
        if (!dest.exists()) {
            throw new FileNotFoundException("Uploaded file does not exist at destination path");
        }
        agent.setNomFichier(uniqueFileName);
        //generate user name
        String username=this.generateUsername(newAgent.getNom(), newAgent.getPrenom());
        agent.setUsername(username);
        String pwd=passwordService.generateProvisionalPassword();

        String encodedPassword=passwordService.encodePassword(pwd);
        agent.setPassword(encodedPassword);
        agentRepository.save(agent);
        //after saving agent send email
        String content="<h1> Hello Agent "+ newAgent.getNom() +" "+newAgent.getPrenom()+" and Welcome To EnsaPay  application.</h1> </br>" +
                " <h3> please use these informations to log In to your Account: </h3>" +
                "<ul>" +
                "<li style='color:blue;'> User Name :  "+username+" </li> " +
                "<li style='color:blue;'> Password : "+pwd+" </li> " +
                "</ul>";
        mailService.sendEmail(newAgent.getEmail(), content);

    }
    public String generateUsername(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("First name and last name must not be null");
        }
        String baseUsername = firstName.substring(0, Math.min(3, firstName.length())).toLowerCase() +
                lastName.substring(0, Math.min(3, lastName.length())).toLowerCase();

        String uuid = UUID.randomUUID().toString().substring(0, 5); // Shortened UUID for uniqueness
        return baseUsername + uuid;
    }
//    public BackOffice getBackOffice(String username){
//        return backOfficeRepository.findByUsername(username).get();
//    }
public BackOffice getBackOffice(String username){
    Optional<BackOffice> backOfficeOptional = backOfficeRepository.findByUsername(username);
    return backOfficeOptional.orElse(null); // Retourne null si aucun backoffice n'est trouvé
}

    public BackOffice saveAdmin(BackOffice backOffice){

        Optional<BackOffice> firstOption = backOfficeRepository.findById(backOffice.getBackOffice_id());
        Optional<BackOffice> secondOption = backOfficeRepository.findByUsername(backOffice.getUsername());
        Optional<BackOffice> thirdOption = Optional.ofNullable(backOfficeRepository.findByEmail(backOffice.getEmail()));


        if(firstOption .isPresent()|| secondOption.isPresent() || thirdOption.isPresent() ){
            throw new IllegalStateException("admin already exists");
        }
        backOffice.setUsername(this.generateUsername(backOffice.getFirstName(), backOffice.getLastName()));
        backOffice.setPassword(passwordService.encodePassword(backOffice.getPassword()));
        return backOfficeRepository.save(backOffice);
    }
    public void sendOtpForPasswordReset(String email){
        Optional<Agent> agentOptional = agentRepository.findByEmail(email);
        if (agentOptional.isPresent()) {
            Agent agent = agentOptional.get();
            OtpRequest otpRequest = new OtpRequest(agent.getUsername(), agent.getNumeroTelephone());
            smsService.sendSMS(otpRequest);
        } else {
            throw new IllegalArgumentException("No agent found with the provided email.");
        }
    }
    //validate otp sent
    public boolean validateOtp(String username, String otp) {
        OtpValidationRequest otpValidationRequest = new OtpValidationRequest(username, otp);
        String validationResponse = smsService.validateOtp(otpValidationRequest);
        return validationResponse.equals("OTP is valid!");
    }
    //change password after first authentication
    public void changePassword(String email, String newPassword) {
        Optional<Agent> agentOptional = agentRepository.findByEmail(email);
        if (agentOptional.isPresent()) {
            Agent agent = agentOptional.get();
            agent.setPassword(passwordService.encodePassword(newPassword));
            agentRepository.save(agent);
        } else {
            throw new IllegalArgumentException("No agent found with the provided email.");
        }
    }
}
