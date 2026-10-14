package pe.clinica.enfermeria;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
public class NotaEnfermeriaForm {
 @NotNull @PastOrPresent @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
 private LocalDateTime fechaHora;
 @NotBlank @Size(max=8000)
 private String contenido;
 
 private Long version;
 @Size(max=1000)
 private String motivoCambio;
 public LocalDateTime getFechaHora() { return fechaHora; }
 public void setFechaHora(LocalDateTime value) { this.fechaHora=value; }
 public String getContenido() { return contenido; }
 public void setContenido(String value) { this.contenido=value; }
 public Long getVersion() { return version; }
 public void setVersion(Long value) { this.version=value; }
 public String getMotivoCambio() { return motivoCambio; }
 public void setMotivoCambio(String value) { this.motivoCambio=value; }

}
