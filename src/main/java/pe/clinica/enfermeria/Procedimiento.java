package pe.clinica.enfermeria;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
@Entity
public class Procedimiento extends RegistroClinico {
 @Column(length=8000)
 private String tipo;
 @Column(length=8000)
 private String descripcion;
 @Column(length=8000)
 private String resultado;
 @Column(length=8000)
 private String observaciones;

 public String getTipo() { return tipo; }
 public void setTipo(String value) { this.tipo = value; }
 public String getDescripcion() { return descripcion; }
 public void setDescripcion(String value) { this.descripcion = value; }
 public String getResultado() { return resultado; }
 public void setResultado(String value) { this.resultado = value; }
 public String getObservaciones() { return observaciones; }
 public void setObservaciones(String value) { this.observaciones = value; }

}
