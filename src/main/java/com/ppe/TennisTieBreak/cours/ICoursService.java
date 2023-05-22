package com.ppe.TennisTieBreak.cours;

import java.util.List;


public interface ICoursService {

	Cours save(Cours cours);

	Cours edit(Cours cours);

	void deleteById(Long id);

	Cours getById(Long id);

	List<Cours> getAll();
	
}
