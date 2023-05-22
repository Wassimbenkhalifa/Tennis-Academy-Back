package com.ppe.TennisTieBreak.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.modelmapper.ModelMapper;


import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "roles")
@Getter @Setter
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole name;

	public Role(){}

	public Role(Integer id, ERole role) {
		this.id = id;
		this.name = role;
	}

	public Role(ERole name) {
		this.name = name;
	}

	public static Role mapDTOtoRole(RoleDTO roleDTO){
		return new ModelMapper().map(roleDTO, Role.class);
	}

}

