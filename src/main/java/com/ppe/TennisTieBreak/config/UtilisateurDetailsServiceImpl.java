package com.ppe.TennisTieBreak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppe.TennisTieBreak.utilisateur.Utilisateur;
import com.ppe.TennisTieBreak.utilisateur.UtilisateurRepository;


@Service
public class UtilisateurDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UtilisateurRepository utilisateurRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Utilisateur utilisateur = utilisateurRepository.findByUsername(username);
		return (utilisateur != null ? UtilisateurDetailsImpl.build(utilisateur) : null);
	}

}
