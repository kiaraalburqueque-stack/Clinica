package pe.clinica.enfermeria;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
@Entity
public class Profesional {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @Column(nullable=false, unique=true)
 private String usuario;
 @Column(nullable=false)
 private String nombre;
 @Column(nullable=false)
 private String passwordHash;
 @Column(nullable=false)
 private String rol;

 public Long getId() { return id; }
 public void setId(Long value) { this.id = value; }
 public String getUsuario() { return usuario; }
 public void setUsuario(String value) { this.usuario = value; }
 public String getNombre() { return nombre; }
 public void setNombre(String value) { this.nombre = value; }
 public String getPasswordHash() { return passwordHash; }
 public void setPasswordHash(String value) { this.passwordHash = value; }
 public String getRol() { return rol; }
 public void setRol(String value) { this.rol = value; }

}
