package pe.clinica.enfermeria;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
public class ValoracionForm {
 @NotNull @PastOrPresent @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
 private LocalDateTime fechaHora;
 @NotBlank @Size(max=200)
 private String estadoGeneral;
 @NotBlank @Size(max=200)
 private String conciencia;
 @Min(0) @Max(10)
 private Integer dolor;
 @Size(max=200)
 private String movilidad;
 @Size(max=200)
 private String alimentacion;
 @Size(max=200)
 private String higiene;
 @Size(max=200)
 private String piel;
 @Pattern(regexp="No evaluado|Bajo|Medio|Alto")
 private String riesgoCaida = "No evaluado";
 @Pattern(regexp="No evaluado|Bajo|Medio|Alto")
 private String riesgoLesiones = "No evaluado";
 @NotBlank @Size(max=4000)
 private String necesidades;
 @Size(max=4000)
 private String observaciones;
 
 private Long version;
 @Size(max=1000)
 private String motivoCambio;
 public LocalDateTime getFechaHora() { return fechaHora; }
 public void setFechaHora(LocalDateTime value) { this.fechaHora=value; }
 public String getEstadoGeneral() { return estadoGeneral; }
 public void setEstadoGeneral(String value) { this.estadoGeneral=value; }
 public String getConciencia() { return conciencia; }
 public void setConciencia(String value) { this.conciencia=value; }
 public Integer getDolor() { return dolor; }
 public void setDolor(Integer value) { this.dolor=value; }
 public String getMovilidad() { return movilidad; }
 public void setMovilidad(String value) { this.movilidad=value; }
 public String getAlimentacion() { return alimentacion; }
 public void setAlimentacion(String value) { this.alimentacion=value; }
 public String getHigiene() { return higiene; }
 public void setHigiene(String value) { this.higiene=value; }
 public String getPiel() { return piel; }
 public void setPiel(String value) { this.piel=value; }
 public String getRiesgoCaida() { return riesgoCaida; }
 public void setRiesgoCaida(String value) { this.riesgoCaida=value; }
 public String getRiesgoLesiones() { return riesgoLesiones; }
 public void setRiesgoLesiones(String value) { this.riesgoLesiones=value; }
 public String getNecesidades() { return necesidades; }
 public void setNecesidades(String value) { this.necesidades=value; }
 public String getObservaciones() { return observaciones; }
 public void setObservaciones(String value) { this.observaciones=value; }
 public Long getVersion() { return version; }
 public void setVersion(Long value) { this.version=value; }
 public String getMotivoCambio() { return motivoCambio; }
 public void setMotivoCambio(String value) { this.motivoCambio=value; }

}
