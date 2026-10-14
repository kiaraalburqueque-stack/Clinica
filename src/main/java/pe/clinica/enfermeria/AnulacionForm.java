package pe.clinica.enfermeria;


import jakarta.validation.constraints.*;
public record AnulacionForm(@NotBlank @Size(max=1000) String motivo) {}
