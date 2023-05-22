package com.ppe.TennisTieBreak.planification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppe.TennisTieBreak.cours.Cours;

@Repository
public interface PlanificationRepository extends JpaRepository<Planification, Long>{

}
