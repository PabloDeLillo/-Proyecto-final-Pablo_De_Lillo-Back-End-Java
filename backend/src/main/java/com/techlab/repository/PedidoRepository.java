package com.techlab.repository;
import com.techlab.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PedidoRepository extends JpaRepository<Pedido,Integer> {
 List<Pedido> findByUsuarioIdOrderByFechaDesc(Integer usuarioId);
 @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
 @org.springframework.data.jpa.repository.Query("select p from Pedido p where p.id = :id")
 Optional<Pedido> findByIdBloqueado(@org.springframework.data.repository.query.Param("id") Integer id);
 }
