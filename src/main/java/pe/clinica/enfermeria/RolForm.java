package pe.clinica.enfermeria;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

public class RolForm {
 @NotBlank @Pattern(regexp="[A-Z][A-Z0-9_]{2,39}")
 private String codigo;
 @NotBlank @Size(max=100)
 private String nombre;
 @Size(max=500)
 private String descripcion;

 private boolean activo = true;
 @NotEmpty
 private Set<@NotNull Permiso> permisos = new LinkedHashSet<>();

 private Long version;
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
 public Long getVersion() { return version; }
 public void setVersion(Long value) { this.version=value; }
}
