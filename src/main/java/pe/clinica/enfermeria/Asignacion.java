package pe.clinica.enfermeria;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
@Entity
public class Asignacion {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @ManyToOne(optional=false)
 private Paciente paciente;
 @ManyToOne(optional=false)
 private Profesional profesional;
 @Column(nullable=false)
 private LocalDate fecha;
 @Column(nullable=false)
 private String turno;
 @Column(nullable=false)
 private String servicio;
 
 private String habitacion;
 
 private String cama;

 public Long getId() { return id; }
 public void setId(Long value) { this.id = value; }
 public Paciente getPaciente() { return paciente; }
 public void setPaciente(Paciente value) { this.paciente = value; }
 public Profesional getProfesional() { return profesional; }
 public void setProfesional(Profesional value) { this.profesional = value; }
 public LocalDate getFecha() { return fecha; }
 public void setFecha(LocalDate value) { this.fecha = value; }
 public String getTurno() { return turno; }
 public void setTurno(String value) { this.turno = value; }
 public String getServicio() { return servicio; }
 public void setServicio(String value) { this.servicio = value; }
 public String getHabitacion() { return habitacion; }
 public void setHabitacion(String value) { this.habitacion = value; }
 public String getCama() { return cama; }
 public void setCama(String value) { this.cama = value; }

}
