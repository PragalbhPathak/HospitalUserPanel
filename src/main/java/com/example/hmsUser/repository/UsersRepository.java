package com.example.hmsUser.repository;

import com.example.hmsUser.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends GenericRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByRole(String roll);
    Optional<Users> findByUserId(Long userId);
    Optional<Users> findByUsername(String username);
}
