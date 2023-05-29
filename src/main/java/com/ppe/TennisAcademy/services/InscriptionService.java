/*package com.ppe.TennisAcademy.services;

import java.util.List;

import com.ppe.TennisAcademy.entities.Inscription;
import com.ppe.TennisAcademy.repositories.InscriptionRepository;
import com.ppe.TennisAcademy.services.ImplServices.IInscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InscriptionService implements IInscriptionService {

	@Autowired
	private InscriptionRepository inscriptionRepository;

	@Override
	public Inscription save(Inscription inscription) {
        return (Inscription) this.inscriptionRepository.save(inscription);

	}

	@Override
	public Inscription edit(Inscription inscription) {
        return (Inscription) this.inscriptionRepository.save(inscription);
	}

	@Override
	public void deleteById(Long id) {
		if (getById(id) != null) {
			this.inscriptionRepository.deleteById(id);
		}
	}

	@Override
	public Inscription getById(Long id) {
		return (Inscription) this.inscriptionRepository.getById(id);
	}

	@Override
	public List<Inscription> getAll() {
       return this.inscriptionRepository.findAll();
	}

}*/