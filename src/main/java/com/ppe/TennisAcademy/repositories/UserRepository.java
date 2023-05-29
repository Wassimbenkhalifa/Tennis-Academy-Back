package com.ppe.TennisAcademy.repositories;

import java.util.List;

import com.ppe.TennisAcademy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository  <T extends User> extends JpaRepository<T, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<T>findByUsernameContainingIgnoreCase(String username);

    List<User> findByVerified(Boolean status);

    User findByResetPasswordToken(String token);
}
