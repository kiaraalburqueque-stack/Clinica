package pe.clinica.enfermeria;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
@Entity
public class Auditoria {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @Column(nullable=false)
 private String tipo;
 @Column(nullable=false)
 private Long registroId;
 @Column(nullable=false)
 private String accion;
 @Column(nullable=false)
 private String usuario;
 @Column(nullable=false)
 private LocalDateTime fechaHora;
 @Column(length=1000)
 private String motivo;
 @Lob
 private String datosAnteriores;

 public Long getId() { return id; }
 public void setId(Long value) { this.id = value; }
 public String getTipo() { return tipo; }
 public void setTipo(String value) { this.tipo = value; }
 public Long getRegistroId() { return registroId; }
 public void setRegistroId(Long value) { this.registroId = value; }
 public String getAccion() { return accion; }
 public void setAccion(String value) { this.accion = value; }
 public String getUsuario() { return usuario; }
 public void setUsuario(String value) { this.usuario = value; }
 public LocalDateTime getFechaHora() { return fechaHora; }
 public void setFechaHora(LocalDateTime value) { this.fechaHora = value; }
 public String getMotivo() { return motivo; }
 public void setMotivo(String value) { this.motivo = value; }
 public String getDatosAnteriores() { return datosAnteriores; }
 public void setDatosAnteriores(String value) { this.datosAnteriores = value; }

}
