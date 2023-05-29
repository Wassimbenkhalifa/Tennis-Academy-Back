package com.ppe.TennisAcademy.entities;

import org.modelmapper.ModelMapper;




import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminDTO extends UserDTO{


    public static AdminDTO mapToAdminDTO(Admin admin){
        return new ModelMapper().map(admin, AdminDTO.class);
    }
}