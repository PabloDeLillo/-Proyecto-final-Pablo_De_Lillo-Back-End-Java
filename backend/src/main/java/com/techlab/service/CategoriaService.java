package com.techlab.service;
import com.techlab.model.Categoria;import com.techlab.repository.*;import com.techlab.exception.*;
import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
public class CategoriaService {
 private final CategoriaRepository categorias;private final ProductoRepository productos;
 public CategoriaService(CategoriaRepository c,ProductoRepository p){categorias=c;productos=p;}
 public List<Categoria> listar(){return categorias.findAll();}
 public Categoria buscar(int id){return categorias.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Categoría no encontrada"));}
 @Transactional public Categoria crear(Categoria c){if(categorias.existsByNombreIgnoreCase(c.getNombre().trim()))throw new ReglaNegocioException("La categoría ya existe");return categorias.save(new Categoria(c.getNombre().trim()));}
 @Transactional public Categoria actualizar(int id,Categoria c){Categoria actual=buscar(id);String nombre=c.getNombre().trim();if(!actual.getNombre().equalsIgnoreCase(nombre)&&categorias.existsByNombreIgnoreCase(nombre))throw new ReglaNegocioException("La categoría ya existe");actual.setNombre(nombre);return categorias.save(actual);}
 @Transactional public void eliminar(int id){Categoria actual=buscar(id);if(productos.existsByCategoriaId(id))throw new ReglaNegocioException("La categoría tiene productos asociados");categorias.delete(actual);}
}
