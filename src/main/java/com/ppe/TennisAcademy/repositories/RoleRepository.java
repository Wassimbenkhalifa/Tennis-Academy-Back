package com.ppe.TennisAcademy.repositories;

import com.ppe.TennisAcademy.entities.ERole;
import com.ppe.TennisAcademy.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository <Role, Long>{
    Optional<Role> findByName(ERole name);
}

