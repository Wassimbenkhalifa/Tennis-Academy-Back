package com.ppe.TennisTieBreak.utilisateur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UtilisateurRepository  <T extends Utilisateur> extends JpaRepository<T, Long> {

	Utilisateur findByUsername(String username);
	
	Utilisateur findByEmail(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	List<T>findByUsernameContainingIgnoreCase(String username);
	
	List<Utilisateur> findByVerified(Boolean status);
	
	Utilisateur findByResetPasswordToken(String token);
	}
