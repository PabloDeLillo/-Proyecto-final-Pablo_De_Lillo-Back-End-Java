package com.techlab.dto;
import com.techlab.model.LineaPedido;
public record LineaResponse(Integer productoId,String nombre,int cantidad,double precioUnitario,double subtotal){
 public static LineaResponse de(LineaPedido l){return new LineaResponse(l.getProducto().getId(),l.getProducto().getNombre(),l.getCantidad(),l.getPrecioUnitario(),l.calcularTotal());}
}
