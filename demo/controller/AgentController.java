package com.example.demo.controller;

import com.example.demo.dto.AgentDto;
import com.example.demo.entity.Agence;
import com.example.demo.entity.Agent;
import com.example.demo.repo.AgenceRepository;
import com.example.demo.repo.AgentRepository;
import com.example.demo.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")//localhost de angular
@RequestMapping("/agents")
public class AgentController {
    @Autowired
    private AgentService agentService;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private AgenceRepository agenceRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addAgent(@RequestBody Agent agent) {
        agentService.addAgent(agent);
        return ResponseEntity.status(HttpStatus.CREATED).body("Agent ajouté avec succès");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAgent(@PathVariable Long id, @RequestBody AgentDto agentDto) {
        Optional<Agent> agent=agentRepository.findAgentById(id);
        agentService.updateAgent(agent.get(), agentDto);
        return ResponseEntity.ok("Agent mis à jour avec succès");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAgent(@PathVariable Long id) {
        agentService.deleteAgent(id);
        return ResponseEntity.ok("Agent supprimé avec succès");
    }

    @GetMapping("/agents-by-agence/{id_agence}")
    public ResponseEntity<List<Agent>> getAgentsByAgence(@PathVariable Long id_agence) {
        List<Agent> agents = agentService.agents(id_agence);
        return ResponseEntity.ok(agents);
    }

    @PostMapping("/add-to-agence")
    public ResponseEntity<String> addAgentToAgence(@RequestBody Agent agent, @RequestParam Long id_agence) {
        Optional<Agence> agence=agenceRepository.findById(id_agence);
        agentService.addAgentToAgence(agent, agence.get());
        return ResponseEntity.status(HttpStatus.CREATED).body("Agent ajouté à l'agence avec succès");
    }

    @DeleteMapping("/delete-from-agence/{id}")
    public ResponseEntity<String> deleteAgentFromAgence(@PathVariable Long id, @RequestBody Agence agence) {
        agentService.deleteAgentFromAgence(id, agence);
        return ResponseEntity.ok("Agent retiré de l'agence avec succès");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Agent>> getAllAgents() {
        List<Agent> agents = agentService.agents();
        return ResponseEntity.ok(agents);
    }


}
