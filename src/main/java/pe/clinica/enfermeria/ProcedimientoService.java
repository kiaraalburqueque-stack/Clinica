package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
@Service
public class ProcedimientoService extends ClinicalService<Procedimiento,ProcedimientoForm> {
 public ProcedimientoService(ProcedimientoRepository repository, AccesoService acceso, AuditoriaRepository auditoria, com.fasterxml.jackson.databind.ObjectMapper mapper) { super(repository, repository, acceso, auditoria, mapper); }
 protected Procedimiento nuevo() { return new Procedimiento(); }
 protected void copiar(ProcedimientoForm form, Procedimiento entity) {
  entity.setFechaHora(form.getFechaHora());
  entity.setTipo(form.getTipo());
  entity.setDescripcion(form.getDescripcion());
  entity.setResultado(form.getResultado());
  entity.setObservaciones(form.getObservaciones());
 }
 public ProcedimientoForm formulario(Procedimiento entity) {
  var form=new ProcedimientoForm(); form.setFechaHora(entity.getFechaHora()); form.setVersion(entity.getVersion());
  form.setTipo(entity.getTipo());
  form.setDescripcion(entity.getDescripcion());
  form.setResultado(entity.getResultado());
  form.setObservaciones(entity.getObservaciones());
  return form;
 }
 protected Long version(ProcedimientoForm form) { return form.getVersion(); }
 protected String motivo(ProcedimientoForm form) { return form.getMotivoCambio(); }
}
