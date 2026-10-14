package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
@Service
public class NotaEnfermeriaService extends ClinicalService<NotaEnfermeria,NotaEnfermeriaForm> {
 public NotaEnfermeriaService(NotaEnfermeriaRepository repository, AccesoService acceso, AuditoriaRepository auditoria, com.fasterxml.jackson.databind.ObjectMapper mapper) { super(repository, repository, acceso, auditoria, mapper); }
 protected NotaEnfermeria nuevo() { return new NotaEnfermeria(); }
 protected void copiar(NotaEnfermeriaForm form, NotaEnfermeria entity) {
  entity.setFechaHora(form.getFechaHora());
  entity.setContenido(form.getContenido());
 }
 public NotaEnfermeriaForm formulario(NotaEnfermeria entity) {
  var form=new NotaEnfermeriaForm(); form.setFechaHora(entity.getFechaHora()); form.setVersion(entity.getVersion());
  form.setContenido(entity.getContenido());
  return form;
 }
 protected Long version(NotaEnfermeriaForm form) { return form.getVersion(); }
 protected String motivo(NotaEnfermeriaForm form) { return form.getMotivoCambio(); }
}
