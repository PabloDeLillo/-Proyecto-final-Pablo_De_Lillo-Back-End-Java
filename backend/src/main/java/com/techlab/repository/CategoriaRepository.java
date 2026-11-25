package com.techlab.repository;
import com.techlab.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoriaRepository extends JpaRepository<Categoria,Integer> { boolean existsByNombreIgnoreCase(String nombre); }
