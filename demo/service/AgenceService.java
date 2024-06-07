package com.example.demo.service;

import com.example.demo.entity.Agence;
import com.example.demo.repo.AgenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgenceService {
    @Autowired
    public AgenceRepository agenceRepository;

    public  void addAgence(Agence agence){
        agenceRepository.save(agence);
    }

}
