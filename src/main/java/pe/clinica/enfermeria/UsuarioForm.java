package pe.clinica.enfermeria;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

public class UsuarioForm {
 @NotBlank @Pattern(regexp="[a-zA-Z0-9._-]{3,40}")
 private String username;
 @NotBlank @Size(max=100)
 private String nombre;
 @NotBlank @Email @Size(max=200)
 private String correo;
 @Size(max=72)
 private String password;

 private boolean activo = true;
 @NotEmpty
 private Set<Long> rolIds = new LinkedHashSet<>();

 private Long version;
 public String getUsername() { return username; }
 public void setUsername(String value) { this.username=value; }
 public String getNombre() { return nombre; }
 public void setNombre(String value) { this.nombre=value; }
 public String getCorreo() { return correo; }
 public void setCorreo(String value) { this.correo=value; }
 public String getPassword() { return password; }
 public void setPassword(String value) { this.password=value; }
 public boolean getActivo() { return activo; }
 public void setActivo(boolean value) { this.activo=value; }
 public Set<Long> getRolIds() { return rolIds; }
 public void setRolIds(Set<Long> value) { this.rolIds=value; }
 public Long getVersion() { return version; }
 public void setVersion(Long value) { this.version=value; }
}
