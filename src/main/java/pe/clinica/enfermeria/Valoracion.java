package pe.clinica.enfermeria;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
@Entity
public class Valoracion extends RegistroClinico {
 @Column(length=8000)
 private String estadoGeneral;
 @Column(length=8000)
 private String conciencia;
 
 private Integer dolor;
 @Column(length=8000)
 private String movilidad;
 @Column(length=8000)
 private String alimentacion;
 @Column(length=8000)
 private String higiene;
 @Column(length=8000)
 private String piel;
 @Column(length=8000)
 private String riesgoCaida;
 @Column(length=8000)
 private String riesgoLesiones;
 @Column(length=8000)
 private String necesidades;
 @Column(length=8000)
 private String observaciones;

 public String getEstadoGeneral() { return estadoGeneral; }
 public void setEstadoGeneral(String value) { this.estadoGeneral = value; }
 public String getConciencia() { return conciencia; }
 public void setConciencia(String value) { this.conciencia = value; }
 public Integer getDolor() { return dolor; }
 public void setDolor(Integer value) { this.dolor = value; }
 public String getMovilidad() { return movilidad; }
 public void setMovilidad(String value) { this.movilidad = value; }
 public String getAlimentacion() { return alimentacion; }
 public void setAlimentacion(String value) { this.alimentacion = value; }
 public String getHigiene() { return higiene; }
 public void setHigiene(String value) { this.higiene = value; }
 public String getPiel() { return piel; }
 public void setPiel(String value) { this.piel = value; }
 public String getRiesgoCaida() { return riesgoCaida; }
 public void setRiesgoCaida(String value) { this.riesgoCaida = value; }
 public String getRiesgoLesiones() { return riesgoLesiones; }
 public void setRiesgoLesiones(String value) { this.riesgoLesiones = value; }
 public String getNecesidades() { return necesidades; }
 public void setNecesidades(String value) { this.necesidades = value; }
 public String getObservaciones() { return observaciones; }
 public void setObservaciones(String value) { this.observaciones = value; }

}
