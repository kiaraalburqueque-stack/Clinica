package pe.clinica.enfermeria;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
@Entity
public class NotaEnfermeria extends RegistroClinico {
 @Column(length=8000)
 private String contenido;

 public String getContenido() { return contenido; }
 public void setContenido(String value) { this.contenido = value; }

}
