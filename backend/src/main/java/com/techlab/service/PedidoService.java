package com.techlab.service;
import com.techlab.model.*;import com.techlab.dto.*;import com.techlab.repository.*;import com.techlab.exception.*;
import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
public class PedidoService {
 private final PedidoRepository pedidos;private final ProductoRepository productos;private final UsuarioRepository usuarios;
 public PedidoService(PedidoRepository p,ProductoRepository pr,UsuarioRepository u){pedidos=p;productos=pr;usuarios=u;}
 @Transactional public PedidoResponse crear(PedidoRequest r){
  Usuario usuario=usuarios.findById(r.usuarioId()).orElseThrow(()->new RecursoNoEncontradoException("Usuario no encontrado"));
  Map<Integer,Integer> cantidades=new LinkedHashMap<>();
  for(LineaRequest l:r.lineas()){
   if(l.productoId()==null||l.cantidad()==null||l.cantidad()<=0)throw new ReglaNegocioException("ID y cantidad deben ser válidos");
   cantidades.merge(l.productoId(),l.cantidad(),(a,b)->{try{return Math.addExact(a,b);}catch(ArithmeticException ex){throw new ReglaNegocioException("Cantidad demasiado grande");}});
  }
  Pedido pedido=new Pedido();pedido.setUsuario(usuario);
  for(var entrada:cantidades.entrySet()){
   Producto producto=productos.findById(entrada.getKey()).orElseThrow(()->new RecursoNoEncontradoException("Producto no encontrado: "+entrada.getKey()));
   if(producto.getStock()<entrada.getValue())throw new StockInsuficienteException("Stock insuficiente de "+producto.getNombre());
   // Se guarda el precio al crear el pedido para conservar el historial aun si cambia el catálogo.
   LineaPedido linea=new LineaPedido();linea.setProducto(producto);linea.setCantidad(entrada.getValue());linea.setPrecioUnitario(producto.getPrecio());pedido.agregarLinea(linea);
  }
  return PedidoResponse.de(pedidos.save(pedido));
 }
 @Transactional(readOnly=true) public List<PedidoResponse> listar(){return pedidos.findAll().stream().map(PedidoResponse::de).toList();}
 @Transactional(readOnly=true) public List<PedidoResponse> historial(int usuarioId){if(!usuarios.existsById(usuarioId))throw new RecursoNoEncontradoException("Usuario no encontrado");return pedidos.findByUsuarioIdOrderByFechaDesc(usuarioId).stream().map(PedidoResponse::de).toList();}
 @Transactional(readOnly=true) public PedidoResponse buscar(int id){return PedidoResponse.de(obtener(id));}
 private Pedido obtener(int id){return pedidos.findByIdBloqueado(id).orElseThrow(()->new RecursoNoEncontradoException("Pedido no encontrado"));}
 @Transactional public PedidoResponse cambiarEstado(int id,EstadoPedido nuevo){
  Pedido pedido=obtener(id);EstadoPedido actual=pedido.getEstado();
  if(actual==nuevo)return PedidoResponse.de(pedido);
  boolean permitido=switch(actual){case PENDIENTE -> nuevo==EstadoPedido.CONFIRMADO||nuevo==EstadoPedido.CANCELADO;
   case CONFIRMADO -> nuevo==EstadoPedido.ENVIADO||nuevo==EstadoPedido.CANCELADO;
   case ENVIADO -> nuevo==EstadoPedido.ENTREGADO;
   case ENTREGADO,CANCELADO -> false;};
  if(!permitido)throw new ReglaNegocioException("Transición de estado no permitida: "+actual+" -> "+nuevo);
  // Al confirmar, se bloquean los productos en orden de ID para proteger el stock ante compras simultáneas.
  if(nuevo==EstadoPedido.CONFIRMADO){
   List<LineaPedido> lineas=new ArrayList<>(pedido.getLineas());lineas.sort(Comparator.comparing(l->l.getProducto().getId()));
   for(LineaPedido l:lineas){Producto p=productos.findByIdBloqueado(l.getProducto().getId()).orElseThrow(()->new RecursoNoEncontradoException("Producto eliminado"));
    if(p.getStock()<l.getCantidad())throw new StockInsuficienteException("Stock insuficiente de "+p.getNombre()+" (disponible: "+p.getStock()+")");
    p.setStock(p.getStock()-l.getCantidad());
   }
  }
  if(nuevo==EstadoPedido.CANCELADO&&actual==EstadoPedido.CONFIRMADO){
   List<LineaPedido> lineas=new ArrayList<>(pedido.getLineas());lineas.sort(Comparator.comparing(l->l.getProducto().getId()));
   for(LineaPedido l:lineas){Producto p=productos.findByIdBloqueado(l.getProducto().getId()).orElseThrow(()->new RecursoNoEncontradoException("Producto eliminado"));p.setStock(Math.addExact(p.getStock(),l.getCantidad()));}
  }
  pedido.setEstado(nuevo);return PedidoResponse.de(pedidos.save(pedido));
 }
}
