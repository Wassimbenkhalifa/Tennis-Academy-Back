package com.ppe.TennisAcademy.services.ImplServices;

import com.ppe.TennisAcademy.entities.Cours;

import java.util.List;


public interface ICoursService {

    Cours save(Cours cours);

    Cours edit(Cours cours);

    void deleteById(Long id);

    Cours getById(Long id);

    List<Cours> getAll();

}
