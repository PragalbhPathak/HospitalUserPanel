package com.example.hmsUser.repository;

import com.example.hmsUser.entity.Billing;

import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends GenericRepository<Billing, Long> {
}
