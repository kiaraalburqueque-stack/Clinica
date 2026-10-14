package pe.clinica.enfermeria;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;
@Entity
public class Usuario implements AuditTarget {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @Version
 private Long version;
 @Column(nullable=false,unique=true,length=40)
 private String username;
 @Column(nullable=false,length=100)
 private String nombre;
 @Column(nullable=false,unique=true,length=200)
 private String correo;
 @Column(nullable=false) @com.fasterxml.jackson.annotation.JsonIgnore
 private String passwordHash;

 private boolean activo = true;
 @OneToOne(optional=false) @JoinColumn(name="profesional_id",nullable=false,unique=true)
 private Profesional profesional;
 @ManyToMany(fetch=FetchType.EAGER) @JoinTable(name="usuario_rol",joinColumns=@JoinColumn(name="usuario_id"),inverseJoinColumns=@JoinColumn(name="rol_id"))
 private Set<Rol> roles = new LinkedHashSet<>();
 public Long getId() { return id; }
 public void setId(Long value) { this.id=value; }
 public Long getVersion() { return version; }
 public void setVersion(Long value) { this.version=value; }
 public String getUsername() { return username; }
 public void setUsername(String value) { this.username=value; }
 public String getNombre() { return nombre; }
 public void setNombre(String value) { this.nombre=value; }
 public String getCorreo() { return correo; }
 public void setCorreo(String value) { this.correo=value; }
 public String getPasswordHash() { return passwordHash; }
 public void setPasswordHash(String value) { this.passwordHash=value; }
 public boolean getActivo() { return activo; }
 public void setActivo(boolean value) { this.activo=value; }
 public Profesional getProfesional() { return profesional; }
 public void setProfesional(Profesional value) { this.profesional=value; }
 public Set<Rol> getRoles() { return roles; }
 public void setRoles(Set<Rol> value) { this.roles=value; }
}
