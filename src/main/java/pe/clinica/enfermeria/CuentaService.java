package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
@Service
@Transactional(readOnly=true)
public class CuentaService {
 private final UsuarioRepository repository;
 public CuentaService(UsuarioRepository repository) {this.repository=repository;}
 public Usuario actual() {
  var auth=SecurityContextHolder.getContext().getAuthentication();
  if(auth==null) throw new AccessDeniedException("Sesión requerida");
  var u=repository.findByUsername(auth.getName()).orElseThrow(() -> new AccessDeniedException("Usuario no autorizado"));
  if(!SecurityRules.habilitado(u)) throw new AccessDeniedException("Cuenta desactivada");return u;
 }
 public boolean puede(String permiso) {return SecurityRules.permiso(actual(),Permiso.valueOf(permiso));}
 public String destino() {
  if(puede("ADMINISTRAR")) return "/admin/usuarios";
  if(puede("ENFERMERIA")) return "/";
  if(puede("PACIENTES")) return "/pacientes";
  if(puede("AUDITORIA")) return "/auditoria";
  return "/cuenta";
 }
}
