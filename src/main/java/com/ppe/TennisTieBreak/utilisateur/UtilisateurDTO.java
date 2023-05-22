package com.ppe.TennisTieBreak.utilisateur;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.ppe.TennisTieBreak.admin.Admin;
import com.ppe.TennisTieBreak.admin.AdminDTO;
import com.ppe.TennisTieBreak.config.Pathconst;
import com.ppe.TennisTieBreak.entraineur.Entraineur;
import com.ppe.TennisTieBreak.entraineur.EntraineurDTO;
import com.ppe.TennisTieBreak.joueur.Joueur;
import com.ppe.TennisTieBreak.joueur.JoueurDTO;
import com.ppe.TennisTieBreak.media.MediaDTO;
import com.ppe.TennisTieBreak.role.ERole;
import com.ppe.TennisTieBreak.role.Role;
import com.ppe.TennisTieBreak.role.RoleDTO;
import com.ppe.TennisTieBreak.terrain.Terrain;
import com.ppe.TennisTieBreak.terrain.TerrainDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor @ToString@NoArgsConstructor
public class UtilisateurDTO {

	private Long idUtilisateur;

    private String username;
    
	private String nom;

    private String prenom;

    private Boolean verified;

    private String password;

    private String email;
    
    private String addresse;
    
	private Date dateEngagement;
	
    private Date dateNaissance;
	
	private float rate;
	
	private int telephone;
	
	private int nbrMatchJoues;
	
	private String gender;

    private Set<Role> roles;

    private MediaDTO photo;
    
    

    public static UtilisateurDTO mapUserToDTO(Principal principal) {

        if (!((UsernamePasswordAuthenticationToken) principal).getAuthorities().isEmpty()) {
            GrantedAuthority authority =
                    ((UsernamePasswordAuthenticationToken) principal).getAuthorities().iterator().next();
            UtilisateurDTO user = null;
            if (authority.getAuthority().toString().equals(ERole.ROLE_ADMIN.toString())) {
                user = new ModelMapper().map(((UsernamePasswordAuthenticationToken) principal).getPrincipal()
                        ,AdminDTO.class);
            } else if(authority.getAuthority().toString().equals(ERole.ROLE_JOUEUR.toString())) { //if (authority.getAuthority().toString().equals(ERole.ROLE_ADMIN.toString())){
                user = new ModelMapper().map(((UsernamePasswordAuthenticationToken) principal).getPrincipal()
                        ,JoueurDTO.class);
            }
            else if(authority.getAuthority().toString().equals(ERole.ROLE_ENTRAINEUR.toString())) { //if (authority.getAuthority().toString().equals(ERole.ROLE_ADMIN.toString())){
                user = new ModelMapper().map(((UsernamePasswordAuthenticationToken) principal).getPrincipal()
                        ,EntraineurDTO.class);
            }
                return user;
        }

        return null;
    }

    public static UtilisateurDTO mapUserToUserRole(Utilisateur user){
        if(user instanceof Admin){
            return AdminDTO.mapToAdminDTO((Admin) user);
        } else if(user instanceof Joueur){
            return JoueurDTO.mapToJoueurDTO((Joueur) user);
        }else if(user instanceof Entraineur){
            return EntraineurDTO.mapToEntraineurDTO((Entraineur) user);
        }
        return UtilisateurDTO.mapToUserDTO(user);
    }

    public static UtilisateurDTO mapToUserDTO(Utilisateur user){
		ModelMapper modelMapper = new ModelMapper();
		return mapToUtilisateurDTOWithPhoto(modelMapper.map(user, UtilisateurDTO.class));
    }
    

	public static UtilisateurDTO mapToUtilisateurDTOWithPhoto(UtilisateurDTO utilisateurDTO) {
		if(utilisateurDTO.getPhoto()!=null) {
			utilisateurDTO.getPhoto()
			.setMediaURL(Pathconst.PATH_IMAGE_USER_PROFILE + utilisateurDTO.getPhoto().getFileName());
		}
		return utilisateurDTO;
	}
}
