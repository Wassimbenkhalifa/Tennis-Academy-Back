package com.ppe.TennisAcademy.repositories;

import com.ppe.TennisAcademy.entities.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long>{
}
