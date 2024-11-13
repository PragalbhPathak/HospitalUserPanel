package com.example.hmsUser.repository;

import com.example.hmsUser.entity.Receptionist;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepository extends GenericRepository<Receptionist,Long>{

// IMPORTANT  we use native query instead of using validations using if else conditions.
//    @Query(value = "SELECT * FROM receptionist WHERE (:a IS NULL OR column_name_a = :a) AND (:b IS NULL OR column_name_b = :b)", nativeQuery = true)
//    List<Receptionist> findAllData(@Param("a") Integer a, @Param("b") Integer b);

}
