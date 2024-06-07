package com.example.demo.service;

import com.example.demo.entity.Agent;
import com.example.demo.entity.BackOffice;
import com.example.demo.repo.AgentRepository;
import com.example.demo.repo.BackOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AgentDetailsService implements UserDetailsService {
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private BackOfficeRepository backOfficeRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Agent agent = agentRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Agent not found"));
//        return new org.springframework.security.core.userdetails.User(agent.getUsername(), agent.getPassword(), new ArrayList<>());
        // Vérifie d'abord dans le repository des agents
        Optional<Agent> agentOpt = agentRepository.findByUsername(username);
        if (agentOpt.isPresent()) {
            Agent agent=agentOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    agent.getUsername(),
                    agent.getPassword(),
                    new ArrayList<>()
            );
        }

        // Si aucun agent n'est trouvé, vérifie dans le repository des backoffices
        Optional<BackOffice> backOfficeOpt = backOfficeRepository.findByUsername(username);

        if (backOfficeOpt.isPresent()) {
            BackOffice backOffice=backOfficeOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    backOffice.getUsername(),
                    backOffice.getPassword(),
                    new ArrayList<>()
            );
        }

        // Si ni agent ni backoffice n'est trouvé, lance une exception
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
