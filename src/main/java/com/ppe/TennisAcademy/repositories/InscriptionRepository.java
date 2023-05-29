package com.ppe.TennisAcademy.repositories;

import com.ppe.TennisAcademy.entities.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscriptionRepository extends JpaRepository  <Inscription, Long> {
}
