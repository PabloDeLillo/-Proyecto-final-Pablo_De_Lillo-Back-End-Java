package com.techlab.controller;
import com.techlab.model.Usuario;import com.techlab.repository.UsuarioRepository;import com.techlab.exception.RecursoNoEncontradoException;
import jakarta.validation.Valid;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/usuarios")
public class UsuarioController {
 private final UsuarioRepository repositorio;public UsuarioController(UsuarioRepository r){repositorio=r;}
 @GetMapping public List<Usuario> listar(){return repositorio.findAll();}
 @GetMapping("/{id}") public Usuario buscar(@PathVariable int id){return repositorio.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Usuario no encontrado"));}
 @PostMapping public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario){return ResponseEntity.status(201).body(repositorio.save(usuario));}
}
