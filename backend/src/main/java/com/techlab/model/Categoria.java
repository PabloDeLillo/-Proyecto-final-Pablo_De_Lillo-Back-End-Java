package com.techlab.model;
import com.techlab.interfaces.Identificable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="categorias")
public class Categoria implements Identificable {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @NotBlank @Size(max=100) @Column(nullable=false,unique=true,length=100) private String nombre;
 public Categoria() {} public Categoria(String nombre) { this.nombre=nombre; }
 public Integer getId(){return id;} public String getNombre(){return nombre;}
 public void setNombre(String nombre){this.nombre=nombre;}
}
