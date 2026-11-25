package com.techlab.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
public record PedidoRequest(@NotNull Integer usuarioId,@NotEmpty List<@Valid LineaRequest> lineas) {}
