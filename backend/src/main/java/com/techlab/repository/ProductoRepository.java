package com.techlab.repository;
import com.techlab.model.Producto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface ProductoRepository extends JpaRepository<Producto,Integer> {
 List<Producto> findByNombreContainingIgnoreCase(String nombre);
 boolean existsByCategoriaId(Integer categoriaId);
 @Query("select p from Producto p where p.stock <= p.stockMinimo") List<Producto> findStockBajo();
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select p from Producto p where p.id = :id") Optional<Producto> findByIdBloqueado(@Param("id") Integer id);
}
