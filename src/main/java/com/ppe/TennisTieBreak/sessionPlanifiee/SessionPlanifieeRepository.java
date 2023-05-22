package com.ppe.TennisTieBreak.sessionPlanifiee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppe.TennisTieBreak.session.Session;

@Repository
public interface SessionPlanifieeRepository extends JpaRepository<SessionPlanifiee, Long>{

}
