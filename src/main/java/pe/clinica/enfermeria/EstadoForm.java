package pe.clinica.enfermeria;

import jakarta.validation.constraints.NotNull;
public record EstadoForm(@NotNull Boolean activo, @NotNull Long version) {}
