package pe.clinica.enfermeria;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties={
 "spring.datasource.url=jdbc:h2:mem:evaluacion2-test;DB_CLOSE_DELAY=-1",
 "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class Evaluacion2IntegrationTest {
 @Autowired MockMvc mvc;
 @Autowired ObjectMapper mapper;
 @Autowired UsuarioRepository usuarios;
 @Autowired RolRepository roles;
 @Autowired ProfesionalRepository profesionales;
 @Autowired AuditoriaRepository auditoria;
 @Autowired AdminService adminService;
 @Autowired IdentityData migration;
 @Autowired PacienteRepository pacientes;
 @Autowired NotaEnfermeriaRepository notas;
 @Autowired jakarta.validation.ValidatorFactory validatorFactory;

 private Map<String,Object> rolForm(String codigo) {
  var m=new LinkedHashMap<String,Object>();m.put("codigo",codigo);m.put("nombre","Rol de prueba");m.put("descripcion","Evaluación 2");m.put("activo",true);m.put("permisos",List.of("PACIENTES"));return m;
 }
 private Map<String,Object> usuarioForm(String username,Long rolId) {
  var m=new LinkedHashMap<String,Object>();m.put("username",username);m.put("nombre","Usuario de prueba");m.put("correo",username+"@example.test");m.put("password","Prueba123!");m.put("activo",true);m.put("rolIds",List.of(rolId));return m;
 }
 private com.fasterxml.jackson.databind.JsonNode crear(String endpoint,Map<String,Object> body) throws Exception {
  var r=mvc.perform(post(endpoint).with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
   .andExpect(status().isCreated()).andExpect(header().exists("Location")).andReturn();return mapper.readTree(r.getResponse().getContentAsString());
 }
 @Test void crudUsuariosRolesYAopSinFiltrarContrasenas() throws Exception {
  var rol=crear("/api/admin/roles",rolForm("PRUEBA_CRUD"));long rid=rol.get("id").asLong();
  var uf=usuarioForm("prueba_crud",rid);var usuario=crear("/api/admin/usuarios",uf);long uid=usuario.get("id").asLong();
  assertThat(usuario.has("passwordHash")).isFalse();assertThat(usuario.get("roles").size()).isEqualTo(1);
  var persisted=usuarios.findById(uid).orElseThrow();assertThat(persisted.getPasswordHash()).startsWith("$2");
  assertThat(persisted.getProfesional()).isNotNull();assertThat(persisted.getRoles()).hasSize(1);
  mvc.perform(get("/api/admin/usuarios/"+uid).with(httpBasic("admin","Demo123!"))).andExpect(status().isOk()).andExpect(jsonPath("$.passwordHash").doesNotExist());
  var rf=rolForm("PRUEBA_CRUD");rf.put("nombre","Rol corregido");rf.put("version",rol.get("version").asLong());
  mvc.perform(put("/api/admin/roles/"+rid).with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rf))).andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Rol corregido"));
  uf.put("nombre","Nombre corregido");uf.put("password","");uf.put("version",usuario.get("version").asLong());
  var edited=mvc.perform(put("/api/admin/usuarios/"+uid).with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(uf)))
   .andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Nombre corregido")).andReturn();
  assertThat(usuarios.findById(uid).orElseThrow().getPasswordHash()).isEqualTo(persisted.getPasswordHash());
  mvc.perform(put("/api/admin/usuarios/"+uid).with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(uf))).andExpect(status().isConflict());
  long version=mapper.readTree(edited.getResponse().getContentAsString()).get("version").asLong();
  mvc.perform(delete("/api/admin/usuarios/"+uid).param("version",Long.toString(version)).with(httpBasic("admin","Demo123!"))).andExpect(status().isNoContent());
  mvc.perform(get("/api/pacientes").with(httpBasic("prueba_crud","Prueba123!"))).andExpect(status().isUnauthorized());
  mvc.perform(patch("/api/admin/usuarios/"+uid+"/estado").with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content("{\"activo\":true,\"version\":"+usuarios.findById(uid).orElseThrow().getVersion()+"}"))
   .andExpect(status().isOk());
  mvc.perform(get("/api/pacientes").with(httpBasic("prueba_crud","Prueba123!"))).andExpect(status().isOk());
  var audit=auditoria.findAll().stream().filter(a -> a.getTipo().equals("Usuario") && a.getRegistroId().equals(uid)).toList();
  assertThat(audit).extracting(Auditoria::getAccion).containsExactlyInAnyOrder("CREAR","MODIFICAR","DESACTIVAR","CAMBIAR_ESTADO");
  assertThat(audit).allMatch(a -> a.getUsuario().equals("admin") && a.getFechaHora()!=null);
  assertThat(audit.stream().filter(a -> a.getDatosAnteriores()!=null).map(Auditoria::getDatosAnteriores)).allMatch(s -> !s.contains("password") && !s.contains("Prueba123!"));
 }
 @Test void rolInactivoRevocaSesionExistenteYPuedeReactivarse() throws Exception {
  var rol=crear("/api/admin/roles",rolForm("PRUEBA_SESION"));long rid=rol.get("id").asLong();
  crear("/api/admin/usuarios",usuarioForm("prueba_sesion",rid));
  var session=(MockHttpSession)mvc.perform(post("/login").with(csrf()).param("username","prueba_sesion").param("password","Prueba123!"))
   .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/pacientes")).andReturn().getRequest().getSession(false);
  mvc.perform(get("/pacientes").session(session)).andExpect(status().isOk());
  mvc.perform(delete("/api/admin/roles/"+rid).param("version",rol.get("version").asText()).with(httpBasic("admin","Demo123!"))).andExpect(status().isNoContent());
  mvc.perform(get("/pacientes").session(session)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?error"));
  mvc.perform(get("/api/pacientes").with(httpBasic("prueba_sesion","Prueba123!"))).andExpect(status().isUnauthorized());
  mvc.perform(patch("/api/admin/roles/"+rid+"/estado").with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content("{\"activo\":true,\"version\":"+roles.findById(rid).orElseThrow().getVersion()+"}"))
   .andExpect(status().isOk());
  mvc.perform(get("/api/pacientes").with(httpBasic("prueba_sesion","Prueba123!"))).andExpect(status().isOk());
 }
 @Test void perfilesYOpcionesDelFrontend() throws Exception {
  for(String path:List.of("/admin/usuarios","/admin/usuarios/nuevo","/admin/roles","/admin/roles/nuevo","/auditoria"))
   mvc.perform(get(path).with(user("admin"))).andExpect(status().isOk());
  long ana=usuarios.findByUsername("ana").orElseThrow().getId();long medico=roles.findByCodigo("MEDICO").orElseThrow().getId();
  mvc.perform(get("/admin/usuarios/"+ana+"/editar").with(user("admin"))).andExpect(status().isOk());
  mvc.perform(get("/admin/roles/"+medico+"/editar").with(user("admin"))).andExpect(status().isOk());
  mvc.perform(get("/pacientes").with(user("medico"))).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Ficha clínica")));
  mvc.perform(get("/pacientes/1/signos-vitales").with(user("medico"))).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("+ Registrar"))));
  mvc.perform(get("/pacientes/1/signos-vitales/nuevo").with(user("medico"))).andExpect(status().isForbidden());
  mvc.perform(post("/api/pacientes/1/notas").with(httpBasic("medico","Demo123!")).contentType(MediaType.APPLICATION_JSON).content("{\"fechaHora\":\"2020-01-01T08:00:00\",\"contenido\":\"No permitido\"}"))
   .andExpect(status().isForbidden());
  mvc.perform(get("/pacientes").with(user("recepcion"))).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Ficha clínica"))));
  mvc.perform(get("/pacientes/1/datos").with(user("recepcion"))).andExpect(status().isOk());
  mvc.perform(get("/api/pacientes/1/notas").with(httpBasic("recepcion","Demo123!"))).andExpect(status().isForbidden());
  for(String name:List.of("ana","medico","recepcion")) {
   mvc.perform(get("/api/admin/usuarios").with(httpBasic(name,"Demo123!"))).andExpect(status().isForbidden());
   mvc.perform(get("/admin/roles").with(user(name))).andExpect(status().isForbidden());
  }
  mvc.perform(get("/api/auditoria").with(httpBasic("ana","Demo123!"))).andExpect(status().isForbidden());
  mvc.perform(get("/api/auditoria").with(httpBasic("supervisor","Demo123!"))).andExpect(status().isOk());
 }
 @Test void redireccionesDeLoginSegunRol() throws Exception {
  var destinos=Map.of("admin","/admin/usuarios","ana","/","medico","/pacientes","recepcion","/pacientes");
  for(var e:destinos.entrySet()) mvc.perform(post("/login").with(csrf()).param("username",e.getKey()).param("password","Demo123!"))
   .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(e.getValue()));
 }
 @Test void validacionDuplicadosUltimoAdministradorYRollbackSinAuditarFallos() throws Exception {
  long recepcion=roles.findByCodigo("RECEPCIONISTA").orElseThrow().getId();
  long antes=auditoria.count();
  var invalido=usuarioForm("prueba_invalid",recepcion);invalido.put("correo","no-es-correo");
  mvc.perform(post("/api/admin/usuarios").with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(invalido))).andExpect(status().isBadRequest());
  invalido=usuarioForm("prueba_invalid",recepcion);invalido.put("password","123");
  mvc.perform(post("/api/admin/usuarios").with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(invalido))).andExpect(status().isBadRequest());
  var duplicado=usuarioForm("ana",recepcion);
  mvc.perform(post("/api/admin/usuarios").with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(duplicado))).andExpect(status().isConflict());
  var admin=usuarios.findByUsername("admin").orElseThrow();
  mvc.perform(delete("/api/admin/usuarios/"+admin.getId()).param("version",admin.getVersion().toString()).with(httpBasic("admin","Demo123!"))).andExpect(status().isConflict());
  var rol=roles.findByCodigo("ADMINISTRADOR").orElseThrow();
  mvc.perform(delete("/api/admin/roles/"+rol.getId()).param("version",rol.getVersion().toString()).with(httpBasic("admin","Demo123!"))).andExpect(status().isConflict());
  assertThat(usuarios.findByUsername("admin").orElseThrow().getActivo()).isTrue();assertThat(roles.findByCodigo("ADMINISTRADOR").orElseThrow().getActivo()).isTrue();
  assertThat(auditoria.count()).isEqualTo(antes);
 }
 @Test void formulariosCrudConCsrfYMensajesDeValidacion() throws Exception {
  long medico=roles.findByCodigo("MEDICO").orElseThrow().getId();
  mvc.perform(post("/admin/usuarios").with(user("admin")).param("username","sin_csrf")).andExpect(status().isForbidden());
  mvc.perform(post("/admin/usuarios").with(user("admin")).with(csrf()).param("username","usuario_web").param("nombre","Usuario Web").param("correo","usuario_web@example.test").param("password","Prueba123!").param("activo","true").param("rolIds",Long.toString(medico)))
   .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/usuarios"));
  var u=usuarios.findByUsername("usuario_web").orElseThrow();
  mvc.perform(post("/admin/usuarios/"+u.getId()).with(user("admin")).with(csrf()).param("username","usuario_web").param("nombre","Nombre editado").param("correo","usuario_web@example.test").param("password","").param("activo","true").param("rolIds",Long.toString(medico)).param("version",u.getVersion().toString()))
   .andExpect(status().is3xxRedirection());
  mvc.perform(post("/admin/roles").with(user("admin")).with(csrf()).param("codigo","WEB_ROL").param("nombre","Rol Web").param("permisos","PACIENTES").param("activo","true"))
   .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/roles"));
  mvc.perform(post("/admin/usuarios").with(user("admin")).with(csrf()).param("username","ana").param("nombre","Duplicado").param("correo","otro@example.test").param("password","Prueba123!").param("rolIds",Long.toString(medico)))
   .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("ya está registrado")));
 }
 @Test void migracionIdempotenteConservaRelacionesYSeguridadDelServicio() {
  long cuentas=usuarios.count(),perfiles=profesionales.count();migration.run();migration.run();
  assertThat(usuarios.count()).isEqualTo(cuentas);assertThat(profesionales.count()).isEqualTo(perfiles);
  assertThat(usuarios.findByUsername("ana").orElseThrow().getProfesional().getId()).isEqualTo(profesionales.findByUsuario("ana").orElseThrow().getId());
  SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("ana","",List.of()));
  try {assertThatThrownBy(() -> adminService.usuarios()).isInstanceOf(AccessDeniedException.class);}
  finally {SecurityContextHolder.clearContext();}
 }
 @Test void migracionDeUnaCuentaAntiguaConservaHashYNotaClinica() {
  var legacy=new Profesional();legacy.setUsuario("legacy_enfermera");legacy.setNombre("Enfermera anterior");legacy.setRol("ENFERMERIA");
  String hash=usuarios.findByUsername("ana").orElseThrow().getPasswordHash();legacy.setPasswordHash(hash);profesionales.saveAndFlush(legacy);
  var nota=new NotaEnfermeria();nota.setPaciente(pacientes.findById(1L).orElseThrow());nota.setProfesional(legacy);nota.setFechaHora(java.time.LocalDateTime.of(2020,1,1,8,0));nota.setContenido("Nota anterior a la migración");notas.saveAndFlush(nota);
  migration.run();migration.run();
  var migrated=usuarios.findByUsername("legacy_enfermera").orElseThrow();
  assertThat(migrated.getProfesional().getId()).isEqualTo(legacy.getId());assertThat(migrated.getPasswordHash()).isEqualTo(hash);
  assertThat(migrated.getRoles()).extracting(Rol::getCodigo).containsExactly("ENFERMERIA");
  var conservada=notas.findById(nota.getId()).orElseThrow();assertThat(conservada.getContenido()).isEqualTo("Nota anterior a la migración");assertThat(conservada.getProfesional().getId()).isEqualTo(legacy.getId());
 }
 @Test void relacionesNulasSeRechazanSinGuardarNiAuditar() throws Exception {
  long cuentas=usuarios.count(),perfiles=profesionales.count(),eventos=auditoria.count();
  var form=usuarioForm("prueba_nulos",1L);form.put("rolIds",Arrays.asList((Long)null));
  mvc.perform(post("/api/admin/usuarios").with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(form))).andExpect(status().isBadRequest());
  var rol=rolForm("ROL_NULOS");rol.put("permisos",Arrays.asList((String)null));
  mvc.perform(post("/api/admin/roles").with(httpBasic("admin","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rol))).andExpect(status().isBadRequest());
  assertThat(usuarios.count()).isEqualTo(cuentas);assertThat(profesionales.count()).isEqualTo(perfiles);assertThat(auditoria.count()).isEqualTo(eventos);
 }
 @Test void fechasInvalidasYParametrosAusentesMuestranErroresClaros() throws Exception {
  mvc.perform(get("/auditoria").param("desde","no-es-fecha").with(user("admin")))
   .andExpect(status().isBadRequest()).andExpect(content().string(org.hamcrest.Matchers.containsString("Revisa las fechas")));
  mvc.perform(get("/api/auditoria").param("desde","no-es-fecha").with(httpBasic("admin","Demo123!")))
   .andExpect(status().isBadRequest()).andExpect(jsonPath("$.mensaje").exists());
  mvc.perform(delete("/api/admin/usuarios/1").with(httpBasic("admin","Demo123!")))
   .andExpect(status().isBadRequest()).andExpect(jsonPath("$.mensaje").exists());
  assertThat(validatorFactory.getClockProvider().getClock().getZone()).isEqualTo(java.time.ZoneId.of("America/Lima"));
 }
}
