package com.ppe.TennisTieBreak.utilisateur;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ppe.TennisTieBreak.admin.Admin;
import com.ppe.TennisTieBreak.entraineur.Entraineur;
import com.ppe.TennisTieBreak.joueur.Joueur;
import com.ppe.TennisTieBreak.role.ERole;
import com.ppe.TennisTieBreak.role.Role;
import com.ppe.TennisTieBreak.role.RoleRepository;



import io.jsonwebtoken.lang.Assert;

@Service
@Transactional
public class UtilisateurService <T extends Utilisateur> {

    @Autowired
    protected UtilisateurRepository<T>utilisateurRepository;
    @Autowired
    protected RoleRepository roleRepository;


    public UtilisateurService(UtilisateurRepository<T> utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
        Assert.notNull(this.utilisateurRepository, "user repository is not implemented "+this.utilisateurRepository.getClass());
    }

    public T save(T user){
        List<String> strRoles = user.getRoles().stream().map((r) -> r.getName().toString()).collect(Collectors.toList());
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_JOUEUR)
                    .orElseThrow(() -> new RuntimeException("Error: Role Joueur is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role admin is not found."));
                        roles.add(adminRole);
                        break;
                    case "ROLE_JOUEUR":
                        Role joueurRole = roleRepository.findByName(ERole.ROLE_JOUEUR)
                                .orElseThrow(() -> new RuntimeException("Error: Role joueur is not found."));
                        roles.add(joueurRole);

                        break;

                    case "ROLE_ENTRAINEUR":
                        Role entraineurRole = roleRepository.findByName(ERole.ROLE_ENTRAINEUR)
                                .orElseThrow(() -> new RuntimeException("Error: Role entraineur is not found."));
                        roles.add(entraineurRole);

                        break;                 

                    default:
                        Role userRoleDefault = roleRepository.findByName(ERole.ROLE_JOUEUR)
                                .orElseThrow(() -> new RuntimeException("Error: default Role (user) is not found."));
                        roles.add(userRoleDefault);
                }
            });
        }

        user.setRoles(roles);
      return  utilisateurRepository.save((T) user);
    }

    public T getByID(Long id){
        return this.utilisateurRepository.findById(id).orElse(null);
    }

    public List<T> getAll(){
        return this.utilisateurRepository.findAll();
    }
    
    public List<Entraineur> getAllEntraineurs(){
    	List<Utilisateur> listUtilis=(List<Utilisateur>) this.utilisateurRepository.findAll();
    	List<Entraineur> listEntrai=new ArrayList<>();
    	
    	for(Utilisateur u:listUtilis) {
    		if(u instanceof Entraineur) {
    			listEntrai.add((Entraineur) u);
    		}
    	}
    	
    	if(listEntrai!=null) {
    		return listEntrai;
    	}
    	else return null;
    	
    }
    
    public List<Joueur> getAllJoueurs(){
    	List<Utilisateur> listUtilis=(List<Utilisateur>) this.utilisateurRepository.findAll();
    	List<Joueur> listJoueur=new ArrayList<>();
    	
    	for(Utilisateur u:listUtilis) {
    		if(u instanceof Joueur) {
    			listJoueur.add((Joueur) u);
    		}
    	}
    	
    	if(listJoueur!=null) {
    		return listJoueur;
    	}
    	else return null;
    	
    }
    
    
    public List<Admin> getAllAdmins(){
    	List<Utilisateur> listUtilis=(List<Utilisateur>) this.utilisateurRepository.findAll();
    	List<Admin> listAdmin=new ArrayList<>();
    	
    	for(Utilisateur u:listUtilis) {
    		if(u instanceof Admin) {
    			listAdmin.add((Admin) u);
    		}
    	}
    	
    	if(listAdmin!=null) {
    		return listAdmin;
    	}
    	else return null;
    	
    }
    

    public void deleteById(Long id){
        this.utilisateurRepository.deleteById(id);
    }




    public List<T> findByUsernameOrDescriptionContainingIgnoreCase(String value){
        return this.utilisateurRepository.findByUsernameContainingIgnoreCase(value);
    }

    public Boolean existsByUsername(String username){
        return this.utilisateurRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email){
        return this.utilisateurRepository.existsByEmail(email);
    }


    public T getByUserName(String userName){
        return (T) utilisateurRepository.findByUsername(userName);
    }
    
    public void updateResetPasswordToken (String token, String email) throws UtilisateurNotFoundExeption {
    	Utilisateur existingUser=utilisateurRepository.findByEmail(email);
    	
    	if(existingUser!=null) {
    		existingUser.setResetPasswordToken(token);
    		utilisateurRepository.save((T) existingUser);
    	}else {
    		throw new UtilisateurNotFoundExeption("could not found user with this email  "+email);
    	}
    }
    
    public Utilisateur get(String resetPasswordToken) {
    	return utilisateurRepository.findByResetPasswordToken(resetPasswordToken);
    }
    
    public void updatePassword(Utilisateur utilisateur,String newPassword) {
    	BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    	
    	String encodedPassword=passwordEncoder.encode(newPassword);
    	utilisateur.setPassword(encodedPassword);
    	utilisateur.setResetPasswordToken(null);
    	utilisateurRepository.save((T)utilisateur);
    }
}
