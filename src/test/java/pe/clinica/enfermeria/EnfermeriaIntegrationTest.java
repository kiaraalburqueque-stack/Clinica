package pe.clinica.enfermeria;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties={
 "spring.datasource.url=jdbc:h2:mem:enfermeria-test;DB_CLOSE_DELAY=-1",
 "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class EnfermeriaIntegrationTest {
 @Autowired MockMvc mvc;
 @Autowired ObjectMapper mapper;
 @Autowired AuditoriaRepository auditoria;
 @Autowired ProfesionalRepository profesionales;
 @Autowired AsignacionRepository asignaciones;
 @Autowired SignoVitalRepository signos;

 private Map<String,Object> datos(String modulo) {
  Map<String,Object> data=new LinkedHashMap<>();
  data.put("fechaHora","2020-01-10T08:00:00");
  switch(modulo) {
   case "signos-vitales" -> {data.put("temperatura",37.2);data.put("dolor",3);}
   case "valoraciones" -> {data.put("estadoGeneral","Estable");data.put("conciencia","Orientado");data.put("necesidades","Apoyo para movilización");data.put("riesgoCaida","Medio");data.put("riesgoLesiones","Bajo");}
   case "procedimientos" -> {data.put("tipo","Higiene");data.put("resultado","Realizado sin incidencias");}
   case "notas" -> data.put("contenido","Paciente consciente.\nSe realizan cuidados.");
   default -> throw new IllegalArgumentException();
  }
  return data;
 }

 @ParameterizedTest
 @ValueSource(strings={"signos-vitales","valoraciones","procedimientos","notas"})
 void crudConAuditoriaConservaHistorialYProtegePacientes(String modulo) throws Exception {
  String base="/api/pacientes/1/"+modulo;
  var data=datos(modulo);
  var result=mvc.perform(post(base).with(httpBasic("ana","Demo123!"))
   .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(data)))
   .andExpect(status().isCreated()).andExpect(header().exists("Location"))
   .andExpect(jsonPath("$.profesional").value("Ana Torres"))
   .andReturn();
  var created=mapper.readTree(result.getResponse().getContentAsString());
  long id=created.get("id").asLong();

  mvc.perform(get(base+"/"+id).with(httpBasic("elena","Demo123!"))).andExpect(status().isForbidden());
  mvc.perform(get("/api/pacientes/2/"+modulo+"/"+id).with(httpBasic("ana","Demo123!"))).andExpect(status().isNotFound());
  mvc.perform(get(base).param("desde","2020-01-10").param("hasta","2020-01-10").with(httpBasic("ana","Demo123!")))
   .andExpect(status().isOk()).andExpect(jsonPath("$.content").isNotEmpty());
  mvc.perform(get(base).param("desde","2020-01-11").param("hasta","2020-01-11").with(httpBasic("ana","Demo123!")))
   .andExpect(status().isOk()).andExpect(jsonPath("$.content").isEmpty());
  mvc.perform(get(base).param("desde","2020-02-01").param("hasta","2020-01-01").with(httpBasic("ana","Demo123!")))
   .andExpect(status().isBadRequest());
  mvc.perform(get(base).param("size","101").with(httpBasic("ana","Demo123!"))).andExpect(status().isBadRequest());

  data.put("version",created.get("version").asLong());data.put("motivoCambio","Corregir transcripción");
  data.put("fechaHora","2020-01-10T08:01:00");
  mvc.perform(put(base+"/"+id).with(httpBasic("ana","Demo123!"))
   .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(data)))
   .andExpect(status().isOk()).andExpect(jsonPath("$.version").value(1));
  mvc.perform(put(base+"/"+id).with(httpBasic("ana","Demo123!"))
   .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(data)))
   .andExpect(status().isConflict());

  mvc.perform(delete(base+"/"+id).with(httpBasic("ana","Demo123!"))
   .contentType(MediaType.APPLICATION_JSON).content("{\"motivo\":\"Registro de prueba\"}"))
   .andExpect(status().isNoContent());
  mvc.perform(get(base+"/"+id).with(httpBasic("ana","Demo123!")))
   .andExpect(status().isOk()).andExpect(jsonPath("$.anulado").value(true));
  mvc.perform(delete(base+"/"+id).with(httpBasic("ana","Demo123!"))
   .contentType(MediaType.APPLICATION_JSON).content("{\"motivo\":\"Registro de prueba\"}"))
   .andExpect(status().isConflict());
  assertThat(auditoria.findAll()).anyMatch(a -> a.getRegistroId().equals(id) && a.getAccion().equals("CORREGIR") && a.getDatosAnteriores()!=null);
 }

 @Test void validacionesClinicasYJsonMalformado() throws Exception {
  String base="/api/pacientes/1/signos-vitales";
  var data=datos("signos-vitales");data.put("dolor",11);
  long antes=signos.count();
  mvc.perform(post(base).with(httpBasic("ana","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(data)))
   .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errores.dolor").exists());
  assertThat(signos.count()).isEqualTo(antes);
  data=datos("signos-vitales");data.put("sistolica",120);
  mvc.perform(post(base).with(httpBasic("ana","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(data))).andExpect(status().isBadRequest());
  data=datos("signos-vitales");data.put("saturacion",101);
  mvc.perform(post(base).with(httpBasic("ana","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(data))).andExpect(status().isBadRequest());
  mvc.perform(post(base).with(httpBasic("ana","Demo123!")).contentType(MediaType.APPLICATION_JSON).content("{\"fechaHora\":\"2020-01-01T08:00:00\"}")).andExpect(status().isBadRequest());
  mvc.perform(post(base).with(httpBasic("ana","Demo123!")).contentType(MediaType.APPLICATION_JSON).content("{malformado")).andExpect(status().isBadRequest());
  mvc.perform(post("/api/pacientes/1/notas").with(httpBasic("ana","Demo123!")).contentType(MediaType.APPLICATION_JSON).content("{\"fechaHora\":\"2020-01-01T08:00:00\",\"contenido\":\"   \"}")).andExpect(status().isBadRequest());
  data=datos("procedimientos");data.put("tipo","Otros");
  mvc.perform(post("/api/pacientes/1/procedimientos").with(httpBasic("ana","Demo123!")).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(data))).andExpect(status().isBadRequest());
 }

 @Test void asignacionesYAutenticacion() throws Exception {
  mvc.perform(get("/api/enfermeria/pacientes-asignados")).andExpect(status().isUnauthorized());
  mvc.perform(get("/api/enfermeria/pacientes-asignados").with(httpBasic("ana","incorrecta"))).andExpect(status().isUnauthorized());
  mvc.perform(get("/api/enfermeria/pacientes-asignados").with(httpBasic("ana","Demo123!")))
   .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
  long elena=profesionales.findByUsuario("elena").orElseThrow().getId();
  mvc.perform(get("/api/enfermeria/pacientes-asignados").param("profesionalId",Long.toString(elena)).with(httpBasic("ana","Demo123!"))).andExpect(status().isForbidden());
  mvc.perform(get("/api/enfermeria/pacientes-asignados").param("profesionalId",Long.toString(elena)).with(httpBasic("supervisor","Demo123!")))
   .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
  mvc.perform(get("/api/enfermeria/pacientes-asignados").param("turno","Noche").with(httpBasic("ana","Demo123!")))
   .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
  assertThat(asignaciones.count()).isEqualTo(42);
 }

 @ParameterizedTest @ValueSource(strings={"signos-vitales","valoraciones","procedimientos","notas"})
 void vistasFormulariosYProteccionCsrf(String modulo) throws Exception {
  String base="/pacientes/1/"+modulo;
  mvc.perform(get(base).with(user("ana").roles("ENFERMERIA"))).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Historial del paciente")));
  mvc.perform(get(base+"/nuevo").with(user("ana").roles("ENFERMERIA")))
   .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("_csrf")));
  mvc.perform(post(base).with(user("ana").roles("ENFERMERIA"))).andExpect(status().isForbidden());
  mvc.perform(get(base+"/nuevo").with(user("elena").roles("ENFERMERIA"))).andExpect(status().isForbidden());
 }

 @Test void interfazGuardaNotaYMuestraContenidoSeguro() throws Exception {
  mvc.perform(get("/login")).andExpect(status().isOk());
  mvc.perform(get("/").with(user("ana").roles("ENFERMERIA"))).andExpect(status().isOk());
  mvc.perform(get("/").with(user("supervisor").roles("SUPERVISOR"))).andExpect(status().isOk());
  mvc.perform(get("/pacientes/1").with(user("ana").roles("ENFERMERIA"))).andExpect(status().isOk());
  mvc.perform(post("/pacientes/1/notas").with(user("ana").roles("ENFERMERIA")).with(csrf())
   .param("fechaHora","2020-01-10T09:00").param("contenido","<script>alert(1)</script>"))
   .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/pacientes/1/notas"));
  mvc.perform(get("/pacientes/1/notas").with(user("ana").roles("ENFERMERIA")))
   .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("&lt;script&gt;")));
  mvc.perform(post("/pacientes/1/notas").with(user("ana").roles("ENFERMERIA")).with(csrf())
   .param("fechaHora","2020-01-10T09:00").param("contenido",""))
   .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Revisa los datos")));
 }
}
