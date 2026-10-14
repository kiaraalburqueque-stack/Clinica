package pe.clinica.enfermeria;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
public class ProcedimientoForm {
 @NotNull @PastOrPresent @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
 private LocalDateTime fechaHora;
 @NotBlank @Pattern(regexp="Curación|Cambio de posición|Higiene|Control de glucosa|Administración de oxígeno|Colocación de dispositivo|Retiro de dispositivo|Otros")
 private String tipo;
 @Size(max=1000)
 private String descripcion;
 @NotBlank @Size(max=4000)
 private String resultado;
 @Size(max=4000)
 private String observaciones;
 
 private Long version;
 @Size(max=1000)
 private String motivoCambio;
 public LocalDateTime getFechaHora() { return fechaHora; }
 public void setFechaHora(LocalDateTime value) { this.fechaHora=value; }
 public String getTipo() { return tipo; }
 public void setTipo(String value) { this.tipo=value; }
 public String getDescripcion() { return descripcion; }
 public void setDescripcion(String value) { this.descripcion=value; }
 public String getResultado() { return resultado; }
 public void setResultado(String value) { this.resultado=value; }
 public String getObservaciones() { return observaciones; }
 public void setObservaciones(String value) { this.observaciones=value; }
 public Long getVersion() { return version; }
 public void setVersion(Long value) { this.version=value; }
 public String getMotivoCambio() { return motivoCambio; }
 public void setMotivoCambio(String value) { this.motivoCambio=value; }
@AssertTrue(message="Describa el procedimiento cuando elija Otros")
 public boolean isDescripcionOtrosValida() { return !"Otros".equals(tipo) || (descripcion!=null && !descripcion.isBlank()); }
}
