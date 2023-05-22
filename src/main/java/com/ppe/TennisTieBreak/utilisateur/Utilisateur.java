package com.ppe.TennisTieBreak.utilisateur;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppe.TennisTieBreak.media.Media;
import com.ppe.TennisTieBreak.role.Role;

import lombok.Data;


@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Role", discriminatorType = DiscriminatorType.STRING, length = 20)
@Table(name = "utilisateurs",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class Utilisateur {



	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUtilisateur;
    
    @Column
    private String prenom;
    
    @Column
	private String nom;
    
    @Column
	private String addresse;
    
    
    @Column
    private Date dateNaissance;
    
    @Column
    private int telephone;
    
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
	private String gender;

    
    @NotBlank
    @Size(max = 20)
    private String username;
    
    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;
    
    @Column(name="reset_password_token")
    @Size(max = 120)
    private String resetPasswordToken;

    private Boolean verified;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Media photo;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    public Utilisateur(String username2, String email2, String encode) {
		this.username=username2;
		this.email=email2;
		this.password=encode;
	}

	public Utilisateur() {
		super();
	}

	public Utilisateur(String prenom, String nom, String addresse, Date dateNaissance, int telephone,
			@NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 20) String username,
			@NotBlank @Size(max = 120) String password, boolean verified) {
		super();
		this.prenom = prenom;
		this.nom = nom;
		this.addresse = addresse;
		this.dateNaissance = dateNaissance;
		this.telephone = telephone;
		this.email = email;
		this.username = username;
		this.password = password;
		this.verified = verified;
	}

	public Utilisateur(String prenom, String nom, String addresse, Date dateNaissance, int telephone,
			@NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 20) String username,
			@NotBlank @Size(max = 120) String password, Boolean verified, Set<Role> roles) {
		super();
		this.prenom = prenom;
		this.nom = nom;
		this.addresse = addresse;
		this.dateNaissance = dateNaissance;
		this.telephone = telephone;
		this.email = email;
		this.username = username;
		this.password = password;
		this.verified = verified;
		this.roles = roles;
	}
	
	
    
    
}
