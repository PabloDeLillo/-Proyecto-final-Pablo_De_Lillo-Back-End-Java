package com.techlab.model;
import com.techlab.interfaces.Calculable;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
@Entity @Table(name="pedidos")
public class Pedido implements Calculable {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @ManyToOne(optional=false) @JoinColumn(name="usuario_id",nullable=false) private Usuario usuario;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private EstadoPedido estado=EstadoPedido.PENDIENTE;
 @Column(nullable=false) private LocalDateTime fecha=LocalDateTime.now();
 @OneToMany(mappedBy="pedido",cascade=CascadeType.ALL,orphanRemoval=true) private List<LineaPedido> lineas=new ArrayList<>();
 public Integer getId(){return id;} public Usuario getUsuario(){return usuario;} public EstadoPedido getEstado(){return estado;}
 public LocalDateTime getFecha(){return fecha;} public List<LineaPedido> getLineas(){return lineas;}
 public void setUsuario(Usuario u){usuario=u;} public void setEstado(EstadoPedido e){estado=e;}
 public void agregarLinea(LineaPedido l){l.setPedido(this);lineas.add(l);}
 @Override public double calcularTotal(){return lineas.stream().mapToDouble(LineaPedido::calcularTotal).sum();}
}
