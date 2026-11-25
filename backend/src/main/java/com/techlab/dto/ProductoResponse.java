package com.techlab.dto;
import com.techlab.model.Producto;
public record ProductoResponse(Integer id,String nombre,String descripcion,double precio,int stock,int stockMinimo,
 String imagenUrl,Integer categoriaId,String categoria) {
 public static ProductoResponse de(Producto p){return new ProductoResponse(p.getId(),p.getNombre(),p.getDescripcion(),p.getPrecio(),p.getStock(),p.getStockMinimo(),p.getImagenUrl(),p.getCategoria().getId(),p.getCategoria().getNombre());}
}
