package com.ppe.TennisAcademy.entities;

import java.security.Principal;
import java.util.Date;
import java.util.Set;

import com.ppe.TennisAcademy.config.Pathconst;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor @ToString@NoArgsConstructor
public class UserDTO {

    private Long idUser;

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

    private MediaDTO photo;

    private Set<Role> roles;





    public static UserDTO mapUserToDTO(Principal principal) {

        if (!((UsernamePasswordAuthenticationToken) principal).getAuthorities().isEmpty()) {
            GrantedAuthority authority =
                    ((UsernamePasswordAuthenticationToken) principal).getAuthorities().iterator().next();
            UserDTO user = null;
            if (authority.getAuthority().toString().equals(ERole.ROLE_ADMIN.toString())) {
                user = new ModelMapper().map(((UsernamePasswordAuthenticationToken) principal).getPrincipal()
                        ,AdminDTO.class);
            } else if(authority.getAuthority().toString().equals(ERole.ROLE_ADHERENT.toString())) { //if (authority.getAuthority().toString().equals(ERole.ROLE_ADMIN.toString())){
                user = new ModelMapper().map(((UsernamePasswordAuthenticationToken) principal).getPrincipal()
                        ,AdherentDTO.class);
            }
            else if(authority.getAuthority().toString().equals(ERole.ROLE_COACH.toString())) { //if (authority.getAuthority().toString().equals(ERole.ROLE_ADMIN.toString())){
                user = new ModelMapper().map(((UsernamePasswordAuthenticationToken) principal).getPrincipal()
                        ,CoachDTO.class);
            }
            return user;
        }

        return null;
    }

    public static UserDTO mapUserToUserRole(User user){
        if(user instanceof Admin){
            return AdminDTO.mapToAdminDTO((Admin) user);
        } else if(user instanceof Adherent){
            return AdherentDTO.mapToAdherentDTO((Adherent) user);
        }else if(user instanceof Coach){
            return CoachDTO.mapToCoachDTO((Coach) user);
        }
        return UserDTO.mapToUserDTO(user);
    }

    public static UserDTO mapToUserDTO(User user){
        ModelMapper modelMapper = new ModelMapper();
        return mapToUserDTOWithPhoto(modelMapper.map(user, UserDTO.class));
    }


    public static UserDTO mapToUserDTOWithPhoto(UserDTO userDTO) {
        if(userDTO.getPhoto()!=null) {
            userDTO.getPhoto()
                    .setMediaURL(Pathconst.PATH_IMAGE_USER_PROFILE + userDTO.getPhoto().getFileName());
        }
        return userDTO;
    }


}