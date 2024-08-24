package com.example.memoryGame.dao;

import com.example.memoryGame.model.Role;
import com.example.memoryGame.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRole(RoleName role);
}
