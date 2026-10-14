package pe.clinica.enfermeria;


import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
@Component @Order(20)
public class IdentityData implements CommandLineRunner {
 private final RolRepository roles;private final UsuarioRepository usuarios;private final ProfesionalRepository profesionales;private final PasswordEncoder encoder;private final boolean demo;
 public IdentityData(RolRepository roles,UsuarioRepository usuarios,ProfesionalRepository profesionales,PasswordEncoder encoder,@Value("${app.seed-demo:false}") boolean demo) {this.roles=roles;this.usuarios=usuarios;this.profesionales=profesionales;this.encoder=encoder;this.demo=demo;}
 @Transactional public void run(String... args) {
  rol("ENFERMERIA","Enfermería",Permiso.ENFERMERIA,Permiso.CONSULTA_CLINICA);
  rol("SUPERVISOR","Supervisor",Permiso.ENFERMERIA,Permiso.CONSULTA_CLINICA,Permiso.SUPERVISION,Permiso.AUDITORIA);
  rol("ADMINISTRADOR","Administrador",Permiso.values());
  rol("MEDICO","Médico",Permiso.PACIENTES,Permiso.CONSULTA_CLINICA);
  rol("RECEPCIONISTA","Recepcionista",Permiso.PACIENTES);
  for(var p:profesionales.findAll()) if(usuarios.findByUsername(p.getUsuario()).isEmpty()) {
   var u=new Usuario();u.setUsername(p.getUsuario());u.setNombre(p.getNombre());u.setCorreo(p.getUsuario()+"@demo.invalid");u.setPasswordHash(p.getPasswordHash());u.setProfesional(p);
   var r=roles.findByCodigo(p.getRol()).orElse(null);if(r!=null) u.getRoles().add(r);else u.setActivo(false);usuarios.save(u);
  }
  if(demo) {crearDemo("admin","Administración","ADMINISTRADOR");crearDemo("medico","Carlos Ramírez","MEDICO");crearDemo("recepcion","Lucía Vega","RECEPCIONISTA");}
 }
 private void rol(String codigo,String nombre,Permiso... permisos) {
  if(roles.findByCodigo(codigo).isPresent()) return;var r=new Rol();r.setCodigo(codigo);r.setNombre(nombre);r.setDescripcion("Rol inicial del sistema");r.setPermisos(new LinkedHashSet<>(Arrays.asList(permisos)));roles.save(r);
 }
 private void crearDemo(String username,String nombre,String codigo) {
  if(usuarios.findByUsername(username).isPresent()) return;
  var hash=encoder.encode("Demo123!");var p=new Profesional();p.setUsuario(username);p.setNombre(nombre);p.setRol(codigo);p.setPasswordHash(hash);profesionales.save(p);
  var u=new Usuario();u.setUsername(username);u.setNombre(nombre);u.setCorreo(username+"@demo.invalid");u.setPasswordHash(hash);u.setProfesional(p);u.getRoles().add(roles.findByCodigo(codigo).orElseThrow());usuarios.save(u);
 }
}
