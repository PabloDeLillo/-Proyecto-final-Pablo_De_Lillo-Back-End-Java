package com.techlab.dto;
import com.techlab.model.*;
import java.time.LocalDateTime;
import java.util.List;
public record PedidoResponse(Integer id,Integer usuarioId,String usuario,LocalDateTime fecha,EstadoPedido estado,double total,List<LineaResponse> lineas){
 public static PedidoResponse de(Pedido p){return new PedidoResponse(p.getId(),p.getUsuario().getId(),p.getUsuario().getNombre(),p.getFecha(),p.getEstado(),p.calcularTotal(),p.getLineas().stream().map(LineaResponse::de).toList());}
}
