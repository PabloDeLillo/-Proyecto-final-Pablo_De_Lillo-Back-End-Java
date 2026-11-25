package com.techlab.model;
import com.techlab.interfaces.Identificable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="productos")
public class Producto implements Identificable {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @NotBlank @Column(nullable=false) private String nombre;
 @Column(length=2000) private String descripcion;
 @DecimalMin("0.0") @Column(nullable=false) private double precio;
 @Min(0) @Column(nullable=false) private int stock;
 @Min(0) @Column(nullable=false) private int stockMinimo;
 @Column(length=1500) private String imagenUrl;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="categoria_id",nullable=false) private Categoria categoria;
 @Version private Long version;
 public Integer getId(){return id;} public String getNombre(){return nombre;} public String getDescripcion(){return descripcion;}
 public double getPrecio(){return precio;} public int getStock(){return stock;} public int getStockMinimo(){return stockMinimo;}
 public String getImagenUrl(){return imagenUrl;} public Categoria getCategoria(){return categoria;}
 public void setNombre(String v){nombre=v;} public void setDescripcion(String v){descripcion=v;}
 public void setPrecio(double v){precio=v;} public void setStock(int v){stock=v;} public void setStockMinimo(int v){stockMinimo=v;}
 public void setImagenUrl(String v){imagenUrl=v;} public void setCategoria(Categoria v){categoria=v;}
}
