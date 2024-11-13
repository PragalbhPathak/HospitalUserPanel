package com.example.hmsUser.repository;

import com.example.hmsUser.entity.Nurse;
import com.example.hmsUser.entity.Patient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NurseRepository extends GenericRepository<Nurse, Long> {
   Optional<Nurse> findByEmail(String email);  // This is already correct.
}