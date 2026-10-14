package pe.clinica.enfermeria;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
public abstract class ClinicalService<T extends RegistroClinico,F> {
 private final JpaRepository<T,Long> repository;
 private final JpaSpecificationExecutor<T> queries;
 private final AccesoService acceso;
 private final AuditoriaRepository auditoria;
 private final ObjectMapper mapper;
 protected ClinicalService(JpaRepository<T,Long> repository, JpaSpecificationExecutor<T> queries, AccesoService acceso, AuditoriaRepository auditoria, ObjectMapper mapper) {
  this.repository=repository; this.queries=queries; this.acceso=acceso; this.auditoria=auditoria; this.mapper=mapper;
 }
 protected abstract T nuevo();
 protected abstract void copiar(F form, T entity);
 protected abstract Long version(F form);
 protected abstract String motivo(F form);
 public abstract F formulario(T entity);
 public static LocalDateTime ahora() { return LocalDateTime.now(ZoneId.of("America/Lima")); }
 @Transactional(readOnly=true)
 public Page<T> historial(Long pacienteId, LocalDate desde, LocalDate hasta, String tipo, int page, int size) {
  acceso.paciente(pacienteId);
  if (desde!=null && hasta!=null && desde.isAfter(hasta)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Intervalo de fechas inválido");
  if (page<0 || size<1 || size>100) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Paginación inválida");
  Specification<T> spec=(r,q,b) -> {
   var condiciones=new ArrayList<jakarta.persistence.criteria.Predicate>();
   condiciones.add(b.equal(r.get("paciente").get("id"),pacienteId));
   if (desde!=null) condiciones.add(b.greaterThanOrEqualTo(r.get("fechaHora"),desde.atStartOfDay()));
   if (hasta!=null) condiciones.add(b.lessThan(r.get("fechaHora"),hasta.plusDays(1).atStartOfDay()));
   if (tipo!=null && !tipo.isBlank()) condiciones.add(b.equal(r.get("tipo"),tipo));
   return b.and(condiciones.toArray(new jakarta.persistence.criteria.Predicate[0]));
  };
  return queries.findAll(spec,PageRequest.of(page,size,Sort.by(Sort.Order.desc("fechaHora"),Sort.Order.desc("id"))));
 }
 @Transactional(readOnly=true)
 public T obtener(Long pacienteId, Long id) {
  acceso.paciente(pacienteId);
  var entity=repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Registro inexistente"));
  if (!entity.getPaciente().getId().equals(pacienteId)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Registro inexistente");
  return entity;
 }
 public T crear(Long pacienteId, F form) {
  var entity=nuevo(); entity.setPaciente(acceso.paciente(pacienteId)); entity.setProfesional(acceso.actual()); copiar(form,entity);
  repository.saveAndFlush(entity); auditar(entity,"CREAR",null,null); return entity;
 }
 public T actualizar(Long pacienteId, Long id, F form) {
  var entity=obtener(pacienteId,id);
  if (entity.getAnulado()) throw new ResponseStatusException(HttpStatus.CONFLICT,"El registro está anulado");
  if (motivo(form)==null || motivo(form).isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Indique el motivo de la corrección");
  if (!Objects.equals(version(form),entity.getVersion())) throw new ResponseStatusException(HttpStatus.CONFLICT,"El registro cambió. Recargue antes de corregirlo");
  var anterior=snapshot(entity); copiar(form,entity); repository.saveAndFlush(entity); auditar(entity,"CORREGIR",motivo(form),anterior); return entity;
 }
 public void anular(Long pacienteId, Long id, String motivo) {
  if (motivo==null || motivo.isBlank() || motivo.length()>1000) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Indique un motivo de hasta 1000 caracteres");
  var entity=obtener(pacienteId,id);
  if (entity.getAnulado()) throw new ResponseStatusException(HttpStatus.CONFLICT,"El registro ya está anulado");
  var anterior=snapshot(entity); entity.setAnulado(true); entity.setMotivoAnulacion(motivo); repository.saveAndFlush(entity); auditar(entity,"ANULAR",motivo,anterior);
 }
 private String snapshot(T entity) {
  try { return mapper.writeValueAsString(formulario(entity)); }
  catch (com.fasterxml.jackson.core.JsonProcessingException e) { throw new IllegalStateException("No se pudo conservar la auditoría",e); }
 }
 private void auditar(T entity, String accion, String motivo, String anterior) {
  var a=new Auditoria(); a.setTipo(entity.getClass().getSimpleName()); a.setRegistroId(entity.getId()); a.setAccion(accion); a.setUsuario(acceso.actual().getUsuario()); a.setFechaHora(ahora()); a.setMotivo(motivo); a.setDatosAnteriores(anterior); auditoria.save(a);
 }
}
