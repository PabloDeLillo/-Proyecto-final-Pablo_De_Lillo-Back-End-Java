package com.techlab.model;
import com.techlab.interfaces.Calculable;
import jakarta.persistence.*;
@Entity @Table(name="lineas_pedido")
public class LineaPedido implements Calculable {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="pedido_id",nullable=false) private Pedido pedido;
 @ManyToOne(optional=false) @JoinColumn(name="producto_id",nullable=false) private Producto producto;
 @Column(nullable=false) private int cantidad;
 @Column(nullable=false) private double precioUnitario;
 public Integer getId(){return id;} public Producto getProducto(){return producto;}
 public int getCantidad(){return cantidad;} public double getPrecioUnitario(){return precioUnitario;}
 public void setPedido(Pedido p){pedido=p;} public void setProducto(Producto p){producto=p;}
 public void setCantidad(int v){cantidad=v;} public void setPrecioUnitario(double v){precioUnitario=v;}
 @Override public double calcularTotal(){return precioUnitario*cantidad;}
}
