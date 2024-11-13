package com.example.hmsUser.repository;

import com.example.hmsUser.entity.Receptionist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptionistRepository extends GenericRepository<Receptionist,Long>{


//    @Query(value = "SELECT * FROM receptionist WHERE (:a IS NULL OR column_name_a = :a) AND (:b IS NULL OR column_name_b = :b)", nativeQuery = true)
//    List<Receptionist> findAllData(@Param("a") Integer a, @Param("b") Integer b);

}
