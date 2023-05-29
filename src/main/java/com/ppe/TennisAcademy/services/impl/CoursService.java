package com.ppe.TennisAcademy.services.impl;

import java.util.List;
import com.ppe.TennisAcademy.entities.Cours;
import com.ppe.TennisAcademy.repositories.CoursRepository;
import com.ppe.TennisAcademy.services.ICoursService;
import org.springframework.stereotype.Service;

@Service
public class CoursService implements ICoursService {

    private final CoursRepository coursRepository;

    public CoursService(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    @Override
    public Cours save(Cours cours) {
        return this.coursRepository.save(cours);

    }

    @Override
    public Cours edit(Cours cours) {
        return this.coursRepository.save(cours);
    }

    @Override
    public void deleteById(Long id) {
        if (getById(id) != null) {
            this.coursRepository.deleteById(id);
        }
    }

    @Override
    public Cours getById(Long id) {
        return this.coursRepository.getReferenceById(id);
    }

    @Override
    public List<Cours> getAll() {
        return this.coursRepository.findAll();
    }

}
