package com.techlab.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(RecursoNoEncontradoException.class)
 public ResponseEntity<Map<String,String>> noEncontrado(Exception e){return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));}
 @ExceptionHandler(ReglaNegocioException.class)
 public ResponseEntity<Map<String,String>> negocio(Exception e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<Map<String,String>> validacion(MethodArgumentNotValidException e){
  String msg=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Datos inválidos");
  return ResponseEntity.badRequest().body(Map.of("error",msg));
 }
 @ExceptionHandler({DataIntegrityViolationException.class,org.springframework.orm.ObjectOptimisticLockingFailureException.class})
 public ResponseEntity<Map<String,String>> conflicto(Exception e){return ResponseEntity.status(409).body(Map.of("error","Conflicto de datos: operación no permitida o registro modificado"));}
 @ExceptionHandler(HttpMessageNotReadableException.class)
 public ResponseEntity<Map<String,String>> formato(Exception e){return ResponseEntity.badRequest().body(Map.of("error","JSON inválido o estado de pedido incorrecto"));}
}
