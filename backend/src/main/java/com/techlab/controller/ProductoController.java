package com.techlab.controller;
import com.techlab.dto.*;import com.techlab.service.ProductoService;import jakarta.validation.Valid;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/productos")
public class ProductoController {
 private final ProductoService servicio;public ProductoController(ProductoService s){servicio=s;}
 @GetMapping public List<ProductoResponse> listar(@RequestParam(required=false) String nombre){return servicio.listar(nombre);}
 @GetMapping("/{id}") public ProductoResponse obtener(@PathVariable int id){return servicio.buscar(id);}
 @GetMapping("/buscar") public List<ProductoResponse> buscar(@RequestParam String nombre){return servicio.listar(nombre);}
 @GetMapping("/stock-bajo") public List<ProductoResponse> stockBajo(){return servicio.stockBajo();}
 @PostMapping public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(r));}
 @PutMapping("/{id}") public ProductoResponse actualizar(@PathVariable int id,@Valid @RequestBody ProductoRequest r){return servicio.actualizar(id,r);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable int id){servicio.eliminar(id);return ResponseEntity.noContent().build();}
}
