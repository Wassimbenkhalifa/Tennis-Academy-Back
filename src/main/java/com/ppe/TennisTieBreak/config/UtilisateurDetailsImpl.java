package com.ppe.TennisTieBreak.config;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppe.TennisTieBreak.utilisateur.Utilisateur;


public class UtilisateurDetailsImpl implements UserDetails{

	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String email;
	private String prenom;
	private String nom;
	private String addresse;


	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UtilisateurDetailsImpl(Long id, String username, String email, String password,
			String prenom, String nom, String addresse, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.prenom = prenom;
		this.nom = nom;
		this.addresse = addresse;
		this.authorities = authorities;
	}

	public static UtilisateurDetailsImpl build(Utilisateur utilisateur) {
		List<GrantedAuthority> authorities = utilisateur.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UtilisateurDetailsImpl(
				utilisateur.getIdUtilisateur(), 
				utilisateur.getUsername(), 
				utilisateur.getEmail(),
				utilisateur.getPassword(), 
				utilisateur.getPrenom(),
				utilisateur.getNom(),
				utilisateur.getAddresse(),
				authorities);
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UtilisateurDetailsImpl utilisateur = (UtilisateurDetailsImpl) o;
		return Objects.equals(id, utilisateur.id);
	}
}
