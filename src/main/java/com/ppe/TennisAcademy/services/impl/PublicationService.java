package com.ppe.TennisAcademy.services.impl;

import java.util.List;

import com.ppe.TennisAcademy.entities.Publication;
import com.ppe.TennisAcademy.repositories.PublicationRepository;
import com.ppe.TennisAcademy.services.IPublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicationService implements IPublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Override
    public Publication save(Publication publication) {
        return (Publication) publicationRepository.save(publication);
    }

    @Override
    public Publication edit(Publication publication) {
        return (Publication) publicationRepository.save(publication);
    }

    @Override
    public void deleteById(Long id) {
        if (getById(id) != null) {
            this.publicationRepository.deleteById(id);
        }
    }

    @Override
    public Publication getById(Long id) {
        return (Publication) this.publicationRepository.getById(id);
    }

    @Override
    public List<Publication> getAll() {
        return publicationRepository.findAll();
    }

}
