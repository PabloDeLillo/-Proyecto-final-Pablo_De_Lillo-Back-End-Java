package com.techlab.controller;
import com.techlab.model.Categoria;import com.techlab.service.CategoriaService;import jakarta.validation.Valid;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/categorias")
public class CategoriaController {
 private final CategoriaService servicio;public CategoriaController(CategoriaService s){servicio=s;}
 @GetMapping public List<Categoria> listar(){return servicio.listar();}
 @GetMapping("/{id}") public Categoria buscar(@PathVariable int id){return servicio.buscar(id);}
 @PostMapping public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria c){return ResponseEntity.status(201).body(servicio.crear(c));}
 @PutMapping("/{id}") public Categoria actualizar(@PathVariable int id,@Valid @RequestBody Categoria c){return servicio.actualizar(id,c);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable int id){servicio.eliminar(id);return ResponseEntity.noContent().build();}
}
