package com.ppe.TennisAcademy.repositories;

import com.ppe.TennisAcademy.entities.Planification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PlanificationRepository extends JpaRepository <Planification, Long> {
}
