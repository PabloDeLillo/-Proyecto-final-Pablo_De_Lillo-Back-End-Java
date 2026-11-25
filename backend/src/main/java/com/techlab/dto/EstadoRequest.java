package com.techlab.dto;
import com.techlab.model.EstadoPedido;
import jakarta.validation.constraints.NotNull;
public record EstadoRequest(@NotNull EstadoPedido estado) {}
