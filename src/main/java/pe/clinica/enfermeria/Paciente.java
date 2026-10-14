package pe.clinica.enfermeria;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
@Entity
public class Paciente {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @Column(nullable=false, unique=true)
 private String codigo;
 @Column(nullable=false, unique=true)
 private String documento;
 @Column(nullable=false)
 private String nombre;
 
 private String sexo;
 
 private LocalDate nacimiento;
 @OneToMany(mappedBy="paciente")
 private java.util.List<Asignacion> asignaciones = new java.util.ArrayList<>();
 @OneToMany(mappedBy="paciente")
 private java.util.List<SignoVital> signosVitales = new java.util.ArrayList<>();
 @OneToMany(mappedBy="paciente")
 private java.util.List<Valoracion> valoraciones = new java.util.ArrayList<>();
 @OneToMany(mappedBy="paciente")
 private java.util.List<Procedimiento> procedimientos = new java.util.ArrayList<>();
 @OneToMany(mappedBy="paciente")
 private java.util.List<NotaEnfermeria> notas = new java.util.ArrayList<>();

 public Long getId() { return id; }
 public void setId(Long value) { this.id = value; }
 public String getCodigo() { return codigo; }
 public void setCodigo(String value) { this.codigo = value; }
 public String getDocumento() { return documento; }
 public void setDocumento(String value) { this.documento = value; }
 public String getNombre() { return nombre; }
 public void setNombre(String value) { this.nombre = value; }
 public String getSexo() { return sexo; }
 public void setSexo(String value) { this.sexo = value; }
 public LocalDate getNacimiento() { return nacimiento; }
 public void setNacimiento(LocalDate value) { this.nacimiento = value; }

}
