package com.ppe.TennisAcademy.services;

import com.ppe.TennisAcademy.entities.Publication;

import java.util.List;

public interface IPublicationService {

    Publication save(Publication publication);

    Publication edit(Publication publication);

    void deleteById(Long id);

    Publication getById(Long id);

    List<Publication> getAll();
}