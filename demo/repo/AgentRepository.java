package com.example.demo.repo;

import com.example.demo.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent,Long> {
    public Optional<Agent> findByEmail(String email);
    @Query("select s from Agent s where s.email=?1")
    Optional<Agent> findAgentByEmail(String email);
    @Query("SELECT s FROM Agent s WHERE s.id=?1")
    Optional<Agent> findAgentById(Long agentId);

    @Query("SELECT s FROM Agent s WHERE s.numeroTelephone=?1")
    Optional<Agent> findAgentByPhone(String phone);

    @Modifying
    @Query("DELETE FROM Agent a WHERE a.email=:email")
    void deleteByEmail(@Param("email") String email);
     //public List<Agent> findAgentsByAgence_Id_agence(Long id);
     @Query("SELECT a FROM Agent a JOIN a.agence ag WHERE ag.id_agence = :id")
     List<Agent> findAgentsByAgenceId(@Param("id") Long id);

    @Query("select s from Agent s where s.agence.id_agence=:id ")
     public Agent findByAgenceId(Long id);
    public Optional<Agent> findByUsername(String username);


}
