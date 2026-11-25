package com.techlab.config;
import com.techlab.model.Categoria;import com.techlab.repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;
@Configuration
public class DatosIniciales {
 // Las categorías se cargan solo si todavía no existen.
 @Bean CommandLineRunner categoriasIniciales(CategoriaRepository repo){return args->{for(String nombre:new String[]{"Electrónica","Periféricos","Alimentos","Limpieza"}){
  if(!repo.existsByNombreIgnoreCase(nombre))repo.save(new Categoria(nombre));
 }};}
}
