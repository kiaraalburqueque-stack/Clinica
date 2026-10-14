package pe.clinica.enfermeria;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;
@Entity
public class Rol implements AuditTarget {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @Version
 private Long version;
 @Column(nullable=false,unique=true,length=40)
 private String codigo;
 @Column(nullable=false,length=100)
 private String nombre;
 @Column(length=500)
 private String descripcion;

 private boolean activo = true;
 @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="rol_permiso",joinColumns=@JoinColumn(name="rol_id")) @Enumerated(EnumType.STRING) @Column(name="permiso",nullable=false)
 private Set<Permiso> permisos = new LinkedHashSet<>();
 @ManyToMany(mappedBy="roles")
 private Set<Usuario> usuarios = new LinkedHashSet<>();
 public Long getId() { return id; }
 public void setId(Long value) { this.id=value; }
 public Long getVersion() { return version; }
 public void setVersion(Long value) { this.version=value; }
 public String getCodigo() { return codigo; }
 public void setCodigo(String value) { this.codigo=value; }
 public String getNombre() { return nombre; }
 public void setNombre(String value) { this.nombre=value; }
 public String getDescripcion() { return descripcion; }
 public void setDescripcion(String value) { this.descripcion=value; }
 public boolean getActivo() { return activo; }
 public void setActivo(boolean value) { this.activo=value; }
 public Set<Permiso> getPermisos() { return permisos; }
 public void setPermisos(Set<Permiso> value) { this.permisos=value; }
 public Set<Usuario> getUsuarios() { return usuarios; }
 public void setUsuarios(Set<Usuario> value) { this.usuarios=value; }
}
