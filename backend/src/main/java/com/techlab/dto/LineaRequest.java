package com.techlab.dto;
import jakarta.validation.constraints.*;
public record LineaRequest(@NotNull Integer productoId, @NotNull @Min(1) Integer cantidad) {}
