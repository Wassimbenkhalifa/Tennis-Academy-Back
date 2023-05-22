package com.ppe.TennisTieBreak.publication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicationService implements IPublicationService{
	
	@Autowired
	private PublicationRepository publicationRepository;

	@Override
	public Publication save(Publication publication) {
		return publicationRepository.save(publication);
	}

	@Override
	public Publication edit(Publication publication) {
		return publicationRepository.save(publication);
	}

	@Override
	public void deleteById(Long id) {
		if (getById(id) != null) {
			this.publicationRepository.deleteById(id);
		}
	}

	@Override
	public Publication getById(Long id) {
		return this.publicationRepository.getById(id);
	}

	@Override
	public List<Publication> getAll() {
		return publicationRepository.findAll();
	}

}
