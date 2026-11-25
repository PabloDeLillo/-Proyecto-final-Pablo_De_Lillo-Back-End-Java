package com.techlab.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="usuarios")
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @NotBlank @Column(nullable=false) private String nombre;
 @NotBlank @Email @Column(nullable=false,unique=true) private String email;
 public Integer getId(){return id;} public String getNombre(){return nombre;} public String getEmail(){return email;}
 public void setNombre(String v){nombre=v;} public void setEmail(String v){email=v;}
}
