//package com.ppe.TennisTieBreak.inscription;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.ppe.TennisTieBreak.commentaire.CommentaireRepository;
//import com.ppe.TennisTieBreak.cours.Cours;
//
//@Service
//public class InscriptionService implements IInscriptionService{
//
//	@Autowired
//	private InscriptionRepository inscriptionRepository;
//	
//	@Override
//	public Inscription save(Inscription inscription) {
//        return this.inscriptionRepository.save(inscription);
//
//	}
//
//	@Override
//	public Inscription edit(Inscription inscription) {
//        return this.inscriptionRepository.save(inscription);
//	}
//
//	@Override
//	public void deleteById(Long id) {
//		if (getById(id) != null) {
//			this.inscriptionRepository.deleteById(id);
//		}
//	}
//
//	@Override
//	public Inscription getById(Long id) {
//		return this.inscriptionRepository.getById(id);
//	}
//
//	@Override
//	public List<Inscription> getAll() {
//        return this.inscriptionRepository.findAll();
//	}
//
//}
