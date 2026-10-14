package pe.clinica.enfermeria;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
public class SignoVitalForm {
 @NotNull @PastOrPresent @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
 private LocalDateTime fechaHora;
 @DecimalMin("0.0")
 private BigDecimal temperatura;
 @Min(1)
 private Integer sistolica;
 @Min(1)
 private Integer diastolica;
 @Min(1)
 private Integer frecuenciaCardiaca;
 @Min(1)
 private Integer frecuenciaRespiratoria;
 @DecimalMin("0.0") @DecimalMax("100.0")
 private BigDecimal saturacion;
 @DecimalMin(value="0.0", inclusive=false)
 private BigDecimal peso;
 @DecimalMin(value="0.0", inclusive=false)
 private BigDecimal talla;
 @Min(0) @Max(10)
 private Integer dolor;
 @DecimalMin("0.0")
 private BigDecimal glucosa;
 
 private Long version;
 @Size(max=1000)
 private String motivoCambio;
 public LocalDateTime getFechaHora() { return fechaHora; }
 public void setFechaHora(LocalDateTime value) { this.fechaHora=value; }
 public BigDecimal getTemperatura() { return temperatura; }
 public void setTemperatura(BigDecimal value) { this.temperatura=value; }
 public Integer getSistolica() { return sistolica; }
 public void setSistolica(Integer value) { this.sistolica=value; }
 public Integer getDiastolica() { return diastolica; }
 public void setDiastolica(Integer value) { this.diastolica=value; }
 public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
 public void setFrecuenciaCardiaca(Integer value) { this.frecuenciaCardiaca=value; }
 public Integer getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
 public void setFrecuenciaRespiratoria(Integer value) { this.frecuenciaRespiratoria=value; }
 public BigDecimal getSaturacion() { return saturacion; }
 public void setSaturacion(BigDecimal value) { this.saturacion=value; }
 public BigDecimal getPeso() { return peso; }
 public void setPeso(BigDecimal value) { this.peso=value; }
 public BigDecimal getTalla() { return talla; }
 public void setTalla(BigDecimal value) { this.talla=value; }
 public Integer getDolor() { return dolor; }
 public void setDolor(Integer value) { this.dolor=value; }
 public BigDecimal getGlucosa() { return glucosa; }
 public void setGlucosa(BigDecimal value) { this.glucosa=value; }
 public Long getVersion() { return version; }
 public void setVersion(Long value) { this.version=value; }
 public String getMotivoCambio() { return motivoCambio; }
 public void setMotivoCambio(String value) { this.motivoCambio=value; }
@AssertTrue(message="Ingrese al menos una medición")
 public boolean isMedicionPresente() { return !(temperatura==null && sistolica==null && diastolica==null && frecuenciaCardiaca==null && frecuenciaRespiratoria==null && saturacion==null && peso==null && talla==null && dolor==null && glucosa==null); }
 @AssertTrue(message="Complete ambos componentes de presión arterial")
 public boolean isPresionCompleta() { return (sistolica==null)==(diastolica==null); }
}
