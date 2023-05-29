package com.ppe.TennisAcademy.repositories;

import com.ppe.TennisAcademy.entities.SeancesLibre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeanceLibreRepository extends JpaRepository <SeancesLibre, Long>{
}
