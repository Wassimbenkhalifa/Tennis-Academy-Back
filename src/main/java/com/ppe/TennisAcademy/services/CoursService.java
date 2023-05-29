package com.ppe.TennisAcademy.services;

import java.util.List;
import com.ppe.TennisAcademy.entities.Cours;
import com.ppe.TennisAcademy.repositories.CoursRepository;
import com.ppe.TennisAcademy.services.ImplServices.ICoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursService implements ICoursService {

    @Autowired
    private CoursRepository coursRepository;

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
        return this.coursRepository.getById(id);
    }

    @Override
    public List<Cours> getAll() {
        return this.coursRepository.findAll();
    }

}
