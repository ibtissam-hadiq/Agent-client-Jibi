package com.example.demo.repo;

import com.example.demo.entity.BackOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackOfficeRepository  extends JpaRepository<BackOffice,Long> {
    Optional<BackOffice> findByUsername(String username);
    BackOffice findByEmail(String email);
    boolean existsByUsername(String username);

   boolean existsByEmail(String email);
}
