package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
@Service @Transactional @PreAuthorize("hasAuthority('ADMINISTRAR')")
public class AdminService {
 private final UsuarioRepository usuarios;private final RolRepository roles;private final ProfesionalRepository profesionales;private final PasswordEncoder encoder;
 public AdminService(UsuarioRepository usuarios,RolRepository roles,ProfesionalRepository profesionales,PasswordEncoder encoder) {this.usuarios=usuarios;this.roles=roles;this.profesionales=profesionales;this.encoder=encoder;}
 @Transactional(readOnly=true) public List<Usuario> usuarios() {return usuarios.findAll();}
 @Transactional(readOnly=true) public List<Rol> roles() {return roles.findAll();}
 @Transactional(readOnly=true) public Usuario usuario(Long id) {return usuarios.findById(id).orElseThrow(() -> error(HttpStatus.NOT_FOUND,"Usuario inexistente"));}
 @Transactional(readOnly=true) public Rol rol(Long id) {return roles.findById(id).orElseThrow(() -> error(HttpStatus.NOT_FOUND,"Rol inexistente"));}
 @Auditable(accion="CREAR") public Usuario crearUsuario(UsuarioForm form) {return guardarUsuario(null,form);}
 @Auditable(accion="MODIFICAR") public Usuario editarUsuario(Long id,UsuarioForm form) {return guardarUsuario(id,form);}
 private Usuario guardarUsuario(Long id,UsuarioForm f) {
  var u=id==null?new Usuario():usuario(id);if(id!=null) version(f.getVersion(),u.getVersion());
  String username=f.getUsername().trim().toLowerCase(Locale.ROOT);String correo=f.getCorreo().trim().toLowerCase(Locale.ROOT);
  if(usuarios.existsByUsernameAndIdNot(username,id==null?-1L:id) || usuarios.existsByCorreoAndIdNot(correo,id==null?-1L:id)) throw error(HttpStatus.CONFLICT,"El usuario o correo ya está registrado");
  var seleccionados=new LinkedHashSet<Rol>();for(Long rid:f.getRolIds()) {var r=rol(rid);if(!r.getActivo()) throw error(HttpStatus.BAD_REQUEST,"Seleccione roles activos");seleccionados.add(r);}
  if(seleccionados.isEmpty()) throw error(HttpStatus.BAD_REQUEST,"Seleccione al menos un rol");
  String password=f.getPassword();if(id==null && (password==null || password.isBlank())) throw error(HttpStatus.BAD_REQUEST,"La contraseña es obligatoria");
  if(password!=null && !password.isEmpty()) {
   if(password.length()<8 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72 || !password.matches("(?s).*[A-Za-z].*") || !password.matches("(?s).*[0-9].*")) throw error(HttpStatus.BAD_REQUEST,"La contraseña debe tener 8 caracteres, letras y números, y no superar 72 bytes");
   u.setPasswordHash(encoder.encode(password));
  }
  u.setUsername(username);u.setNombre(f.getNombre().trim());u.setCorreo(correo);u.setActivo(f.getActivo());u.setRoles(seleccionados);
  var p=id==null?new Profesional():u.getProfesional();p.setUsuario(username);p.setNombre(u.getNombre());p.setPasswordHash(u.getPasswordHash());p.setRol(seleccionados.iterator().next().getCodigo());profesionales.saveAndFlush(p);u.setProfesional(p);
  usuarios.saveAndFlush(u);garantizarAdministrador();return u;
 }
 @Auditable(accion="CAMBIAR_ESTADO") public Usuario estadoUsuario(Long id,boolean activo,Long version) {var u=usuario(id);version(version,u.getVersion());u.setActivo(activo);usuarios.saveAndFlush(u);garantizarAdministrador();return u;}
 @Auditable(accion="DESACTIVAR") public Usuario desactivarUsuario(Long id,Long version) {var u=usuario(id);version(version,u.getVersion());u.setActivo(false);usuarios.saveAndFlush(u);garantizarAdministrador();return u;}
 @Auditable(accion="CREAR") public Rol crearRol(RolForm form) {return guardarRol(null,form);}
 @Auditable(accion="MODIFICAR") public Rol editarRol(Long id,RolForm form) {return guardarRol(id,form);}
 private Rol guardarRol(Long id,RolForm f) {
  var r=id==null?new Rol():rol(id);if(id!=null) version(f.getVersion(),r.getVersion());
  if(roles.existsByCodigoAndIdNot(f.getCodigo(),id==null?-1L:id)) throw error(HttpStatus.CONFLICT,"El código de rol ya existe");
  if(f.getPermisos().isEmpty()) throw error(HttpStatus.BAD_REQUEST,"Seleccione al menos un permiso");
  r.setCodigo(f.getCodigo());r.setNombre(f.getNombre().trim());r.setDescripcion(f.getDescripcion());r.setActivo(f.getActivo());r.setPermisos(new LinkedHashSet<>(f.getPermisos()));roles.saveAndFlush(r);garantizarAdministrador();return r;
 }
 @Auditable(accion="CAMBIAR_ESTADO") public Rol estadoRol(Long id,boolean activo,Long version) {var r=rol(id);version(version,r.getVersion());r.setActivo(activo);roles.saveAndFlush(r);garantizarAdministrador();return r;}
 @Auditable(accion="DESACTIVAR") public Rol desactivarRol(Long id,Long version) {var r=rol(id);version(version,r.getVersion());r.setActivo(false);roles.saveAndFlush(r);garantizarAdministrador();return r;}
 private void garantizarAdministrador() {if(usuarios.findAll().stream().noneMatch(u -> SecurityRules.permiso(u,Permiso.ADMINISTRAR))) throw error(HttpStatus.CONFLICT,"Debe quedar al menos un administrador activo");}
 private void version(Long enviada,Long actual) {if(!Objects.equals(enviada,actual)) throw error(HttpStatus.CONFLICT,"El registro cambió. Recargue antes de editarlo");}
 private ResponseStatusException error(HttpStatus status,String mensaje) {return new ResponseStatusException(status,mensaje);}
 public UsuarioForm formulario(Usuario u) {var f=new UsuarioForm();f.setUsername(u.getUsername());f.setNombre(u.getNombre());f.setCorreo(u.getCorreo());f.setActivo(u.getActivo());f.setVersion(u.getVersion());u.getRoles().forEach(r -> f.getRolIds().add(r.getId()));return f;}
 public RolForm formulario(Rol r) {var f=new RolForm();f.setCodigo(r.getCodigo());f.setNombre(r.getNombre());f.setDescripcion(r.getDescripcion());f.setActivo(r.getActivo());f.setVersion(r.getVersion());f.setPermisos(new LinkedHashSet<>(r.getPermisos()));return f;}
}
