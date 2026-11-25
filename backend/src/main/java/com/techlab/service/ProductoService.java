package com.techlab.service;
import com.techlab.model.*;import com.techlab.dto.*;import com.techlab.repository.*;import com.techlab.exception.*;
import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
public class ProductoService {
 private final ProductoRepository productos; private final CategoriaRepository categorias;
 public ProductoService(ProductoRepository productos,CategoriaRepository categorias){this.productos=productos;this.categorias=categorias;}
 @Transactional(readOnly=true) public List<ProductoResponse> listar(String nombre){return (nombre==null||nombre.isBlank()?productos.findAll():productos.findByNombreContainingIgnoreCase(nombre)).stream().map(ProductoResponse::de).toList();}
 @Transactional(readOnly=true) public ProductoResponse buscar(int id){return ProductoResponse.de(obtener(id));}
 @Transactional(readOnly=true) public List<ProductoResponse> stockBajo(){return productos.findStockBajo().stream().map(ProductoResponse::de).toList();}
 public Producto obtener(int id){return productos.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Producto no encontrado: "+id));}
 @Transactional public ProductoResponse crear(ProductoRequest r){return ProductoResponse.de(productos.save(aplicar(new Producto(),r)));}
 @Transactional public ProductoResponse actualizar(int id,ProductoRequest r){return ProductoResponse.de(productos.save(aplicar(obtener(id),r)));}
 private Producto aplicar(Producto p,ProductoRequest r){
  if(!Double.isFinite(r.precio())||r.precio()<0)throw new ReglaNegocioException("El precio debe ser válido y no negativo");
  Categoria c=categorias.findById(r.categoriaId()).orElseThrow(()->new RecursoNoEncontradoException("Categoría no encontrada"));
  p.setNombre(r.nombre().trim());p.setDescripcion(r.descripcion());p.setPrecio(r.precio());p.setStock(r.stock());
  p.setStockMinimo(r.stockMinimo()==null?0:r.stockMinimo());p.setCategoria(c);p.setImagenUrl(r.imagenUrl());return p;
 }
 @Transactional public void eliminar(int id){Producto p=obtener(id);productos.delete(p);productos.flush();}
}
