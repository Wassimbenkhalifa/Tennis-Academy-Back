package com.ppe.TennisAcademy.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Entity
@Data
@DiscriminatorValue("ADMIN")
public class Admin extends User {


    public Admin() {
        super();
    }

   /* public static Admin mapToAdmin(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(user, Admin.class));
    }*/
}
