package com.example.hmsUser.repository;

import com.example.hmsUser.entity.Patient;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends GenericRepository<Patient, Long> {
    // Find a patient by email
    Patient findByEmail(String email);
}
