package com.ppe.TennisTieBreak.role;

import org.modelmapper.ModelMapper;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoleDTO {

    private Integer id;

    private ERole name;

    public RoleDTO(){}

    public RoleDTO(String name){
        if(ERole.valueOf(name) != null){
            this.name = ERole.valueOf(name);
        }else{
            throw new IllegalArgumentException("error role name parameter");
        }
    }

    public RoleDTO(ERole name) {
        this.name = name;
    }

    public static RoleDTO mapRoleToRoleDTO(Role role){
        return new ModelMapper().map(role, RoleDTO.class);
    }
}
