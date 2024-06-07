package com.example.demo.service;

import com.example.demo.dto.AgentDto;
import com.example.demo.entity.Agence;
import com.example.demo.entity.Agent;
import com.example.demo.repo.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgentService {
    @Autowired
    public AgentRepository agentRepository;
    @Autowired
    public PasswordService passwordService;
    public void addAgent(Agent agent){
        Optional<Agent> opt1=agentRepository.findAgentById(agent.getId());
        Optional<Agent> opt2=agentRepository.findAgentByEmail(agent.getEmail());
        Optional<Agent> opt3=agentRepository.findAgentByPhone(agent.getNumeroTelephone());
        if(opt1.isPresent() || opt2.isPresent() || opt3.isPresent()){
            throw  new IllegalStateException("agent already exist");
        }else{
            String provisionalpwd=passwordService.generateProvisionalPassword();
            agent.setPassword(passwordService.encodePassword(provisionalpwd));
            agent.setUsername(agent.getEmail());
            agent.setValidated(true);
            agent.setCreatedAt(LocalDateTime.now());
            agentRepository.save(agent);
        }



    }
    public Agent updateAgent(Agent existedAgent, AgentDto agentDto){
         String nom=agentDto.getNom();
         if(nom!=null){
             existedAgent.setNom(agentDto.getNom());
         }
        String prenom=agentDto.getPrenom();
        if(nom!=null){
            existedAgent.setPrenom(agentDto.getPrenom());
        }
        String email= agentDto.getEmail();
        if(email!=null){
            existedAgent.setEmail(agentDto.getEmail());
        }
        String telephone=agentDto.getNumeroTelephone();
        if (telephone!=null){
            existedAgent.setNumeroTelephone(agentDto.getNumeroTelephone());
        }
        String description=agentDto.getDescription();
        if (description!=null){
            existedAgent.setDescription(agentDto.getDescription());
        }
        LocalDate dateforbirth= LocalDate.from(agentDto.getDateofbirth());
        if (dateforbirth!=null){
            existedAgent.setDateofbirth(LocalDate.from(agentDto.getDateofbirth()));
        }
        String patente=agentDto.getPatente();
        if (patente!=null){
            existedAgent.setPatente(agentDto.getPatente());
        }
        String adress=agentDto.getAdresse();
        if (adress!=null){
            existedAgent.setAdresse(agentDto.getAdresse());
        }
        String immatr=agentDto.getImmatriculation();
        if (immatr!=null){
            existedAgent.setImmatriculation(agentDto.getImmatriculation());
        }
        String fileName=agentDto.getNomFichier();
        if (fileName!=null){
            existedAgent.setNomFichier(agentDto.getNomFichier());
        }





       return  agentRepository.save(existedAgent);
    }
    public void deleteAgent(Long id){
        Optional<Agent> opt1 = agentRepository.findAgentById(id);
        if(opt1.isPresent()){
            agentRepository.deleteById(id);
        }else {
            throw new IllegalStateException("This Id doesn't exist!");
        }
    }
    public List<Agent> agents(Long id_agence){
        return agentRepository.findAgentsByAgenceId(id_agence);
    }
   @Transactional
    public Agent addAgentToAgence(Agent agent, Agence agence){
       if(agentRepository.findByAgenceId(agence.getId_agence())!=null){
            throw new IllegalStateException("agent exist deja dans cette agence");
        }
       return agentRepository.save(agent);
    }
    public void deleteAgentFromAgence(Long id ,Agence agence){
        if (agentRepository.findByAgenceId(agence.getId_agence())==null){
            throw new IllegalStateException("agent n'exist pas");
        }
        agentRepository.deleteById(id);
    }
    public List<Agent> agents(){
        return agentRepository.findAll();
    }

}
