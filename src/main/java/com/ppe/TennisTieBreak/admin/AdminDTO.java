package com.ppe.TennisTieBreak.admin;


import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.utilisateur.UtilisateurDTO;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminDTO extends UtilisateurDTO{

	
    public static AdminDTO mapToAdminDTO(Admin admin){
        return new ModelMapper().map(admin, AdminDTO.class);
    }
}
