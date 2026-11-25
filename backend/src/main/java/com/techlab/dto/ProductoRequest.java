package com.techlab.dto;
import jakarta.validation.constraints.*;
public record ProductoRequest(@NotBlank String nombre, String descripcion, @NotNull @DecimalMin("0.0") Double precio,
 @NotNull @Min(0) Integer stock, @NotNull Integer categoriaId, String imagenUrl, @Min(0) Integer stockMinimo) {}
