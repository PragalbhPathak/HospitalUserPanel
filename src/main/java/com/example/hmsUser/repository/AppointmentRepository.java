package com.example.hmsUser.repository;

import com.example.hmsUser.entity.Appointment;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends GenericRepository<Appointment, Long> {

}
