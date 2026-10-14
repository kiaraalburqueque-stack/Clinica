package pe.clinica.enfermeria;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
@Entity
public class SignoVital extends RegistroClinico {
 
 private BigDecimal temperatura;
 
 private Integer sistolica;
 
 private Integer diastolica;
 
 private Integer frecuenciaCardiaca;
 
 private Integer frecuenciaRespiratoria;
 
 private BigDecimal saturacion;
 
 private BigDecimal peso;
 
 private BigDecimal talla;
 
 private Integer dolor;
 
 private BigDecimal glucosa;

 public BigDecimal getTemperatura() { return temperatura; }
 public void setTemperatura(BigDecimal value) { this.temperatura = value; }
 public Integer getSistolica() { return sistolica; }
 public void setSistolica(Integer value) { this.sistolica = value; }
 public Integer getDiastolica() { return diastolica; }
 public void setDiastolica(Integer value) { this.diastolica = value; }
 public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
 public void setFrecuenciaCardiaca(Integer value) { this.frecuenciaCardiaca = value; }
 public Integer getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
 public void setFrecuenciaRespiratoria(Integer value) { this.frecuenciaRespiratoria = value; }
 public BigDecimal getSaturacion() { return saturacion; }
 public void setSaturacion(BigDecimal value) { this.saturacion = value; }
 public BigDecimal getPeso() { return peso; }
 public void setPeso(BigDecimal value) { this.peso = value; }
 public BigDecimal getTalla() { return talla; }
 public void setTalla(BigDecimal value) { this.talla = value; }
 public Integer getDolor() { return dolor; }
 public void setDolor(Integer value) { this.dolor = value; }
 public BigDecimal getGlucosa() { return glucosa; }
 public void setGlucosa(BigDecimal value) { this.glucosa = value; }

}
