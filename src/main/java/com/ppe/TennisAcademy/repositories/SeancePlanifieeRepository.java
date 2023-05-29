package com.ppe.TennisAcademy.repositories;

import com.ppe.TennisAcademy.entities.SeancePlanifiee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeancePlanifieeRepository extends JpaRepository <SeancePlanifiee, Long> {
}
