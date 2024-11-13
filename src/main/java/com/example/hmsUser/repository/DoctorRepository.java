package com.example.hmsUser.repository;

import com.example.hmsUser.entity.Doctor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends GenericRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
}