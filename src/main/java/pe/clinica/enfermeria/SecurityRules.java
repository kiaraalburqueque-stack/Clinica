package pe.clinica.enfermeria;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.*;
public final class SecurityRules {
 private SecurityRules() {}
 public static boolean habilitado(Usuario u) {return u.getActivo() && u.getRoles().stream().anyMatch(Rol::getActivo);}
 public static Set<SimpleGrantedAuthority> authorities(Usuario u) {
  Set<SimpleGrantedAuthority> result=new LinkedHashSet<>();
  if(habilitado(u)) for(var r:u.getRoles()) if(r.getActivo()) {
   result.add(new SimpleGrantedAuthority("ROLE_"+r.getCodigo()));
   r.getPermisos().forEach(p -> result.add(new SimpleGrantedAuthority(p.name())));
  }
  return result;
 }
 public static boolean permiso(Usuario u,Permiso p) {return authorities(u).contains(new SimpleGrantedAuthority(p.name()));}
}
