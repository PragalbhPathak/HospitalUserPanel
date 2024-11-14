package com.example.hmsUser.repository;

import com.example.hmsUser.entity.Receptionist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepository extends GenericRepository<Receptionist,Long>{

// IMPORTANT  we use native query instead of using validations using if else conditions.
//    @Query(value = "SELECT * FROM receptionist WHERE (:a IS NULL OR column_name_a = :a) AND (:b IS NULL OR column_name_b = :b)", nativeQuery = true)
//    List<Receptionist> findAllData(@Param("a") Integer a, @Param("b") Integer b);


//    // Native query to check if a Receptionist with a given ID exists
//    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Receptionist WHERE id = :receptionistId", nativeQuery = true)
//    boolean existsByReceptionistId(@Param("receptionistId") Long receptionistId);
//
//    // Native query to validate contact number format (10-12 digits)
//    @Query(value = "SELECT CASE WHEN r.contact REGEXP '^[0-9]{10,12}$' THEN TRUE ELSE FALSE END FROM Receptionist r WHERE r.id = :receptionistId", nativeQuery = true)
//    boolean isContactValid(@Param("receptionistId") Long receptionistId);
//
//    // Native query to validate gender (Male, Female, Other)
//    @Query(value = "SELECT CASE WHEN r.gender IN ('Male', 'Female', 'Other') THEN TRUE ELSE FALSE END FROM Receptionist r WHERE r.id = :receptionistId", nativeQuery = true)
//    boolean isGenderValid(@Param("receptionistId") Long receptionistId);
//
//    // Native query to validate if the age is non-negative
//    @Query(value = "SELECT CASE WHEN r.age >= 0 THEN TRUE ELSE FALSE END FROM Receptionist r WHERE r.id = :receptionistId", nativeQuery = true)
//    boolean isAgeValid(@Param("receptionistId") Long receptionistId);

}
