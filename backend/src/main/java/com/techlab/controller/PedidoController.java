package com.techlab.controller;
import com.techlab.dto.*;import com.techlab.service.PedidoService;import jakarta.validation.Valid;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController
public class PedidoController {
 private final PedidoService servicio;public PedidoController(PedidoService s){servicio=s;}
 @PostMapping("/api/pedidos") public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest r){return ResponseEntity.status(201).body(servicio.crear(r));}
 @GetMapping("/api/pedidos") public List<PedidoResponse> listar(){return servicio.listar();}
 @GetMapping("/api/pedidos/{id}") public PedidoResponse buscar(@PathVariable int id){return servicio.buscar(id);}
 @GetMapping("/api/usuarios/{usuarioId}/pedidos") public List<PedidoResponse> historial(@PathVariable int usuarioId){return servicio.historial(usuarioId);}
 @PatchMapping("/api/pedidos/{id}/estado") public PedidoResponse estado(@PathVariable int id,@Valid @RequestBody EstadoRequest r){return servicio.cambiarEstado(id,r.estado());}
}
