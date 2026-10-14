package pe.clinica.enfermeria;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
@MappedSuperclass
public abstract class RegistroClinico implements AuditTarget {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @Version
 private Long version;
 @ManyToOne(optional=false)
 private Paciente paciente;
 @ManyToOne(optional=false)
 private Profesional profesional;
 @Column(nullable=false)
 private LocalDateTime fechaHora;
 
 private boolean anulado;
 @Column(length=1000)
 private String motivoAnulacion;

 public Long getId() { return id; }
 public void setId(Long value) { this.id = value; }
 public Long getVersion() { return version; }
 public void setVersion(Long value) { this.version = value; }
 public Paciente getPaciente() { return paciente; }
 public void setPaciente(Paciente value) { this.paciente = value; }
 public Profesional getProfesional() { return profesional; }
 public void setProfesional(Profesional value) { this.profesional = value; }
 public LocalDateTime getFechaHora() { return fechaHora; }
 public void setFechaHora(LocalDateTime value) { this.fechaHora = value; }
 public boolean getAnulado() { return anulado; }
 public void setAnulado(boolean value) { this.anulado = value; }
 public String getMotivoAnulacion() { return motivoAnulacion; }
 public void setMotivoAnulacion(String value) { this.motivoAnulacion = value; }

}
