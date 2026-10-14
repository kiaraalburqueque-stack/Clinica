package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
@Service
public class ValoracionService extends ClinicalService<Valoracion,ValoracionForm> {
 public ValoracionService(ValoracionRepository repository, AccesoService acceso, com.fasterxml.jackson.databind.ObjectMapper mapper) { super(repository, repository, acceso, mapper); }
 protected Valoracion nuevo() { return new Valoracion(); }
 protected void copiar(ValoracionForm form, Valoracion entity) {
  entity.setFechaHora(form.getFechaHora());
  entity.setEstadoGeneral(form.getEstadoGeneral());
  entity.setConciencia(form.getConciencia());
  entity.setDolor(form.getDolor());
  entity.setMovilidad(form.getMovilidad());
  entity.setAlimentacion(form.getAlimentacion());
  entity.setHigiene(form.getHigiene());
  entity.setPiel(form.getPiel());
  entity.setRiesgoCaida(form.getRiesgoCaida());
  entity.setRiesgoLesiones(form.getRiesgoLesiones());
  entity.setNecesidades(form.getNecesidades());
  entity.setObservaciones(form.getObservaciones());
 }
 public ValoracionForm formulario(Valoracion entity) {
  var form=new ValoracionForm(); form.setFechaHora(entity.getFechaHora()); form.setVersion(entity.getVersion());
  form.setEstadoGeneral(entity.getEstadoGeneral());
  form.setConciencia(entity.getConciencia());
  form.setDolor(entity.getDolor());
  form.setMovilidad(entity.getMovilidad());
  form.setAlimentacion(entity.getAlimentacion());
  form.setHigiene(entity.getHigiene());
  form.setPiel(entity.getPiel());
  form.setRiesgoCaida(entity.getRiesgoCaida());
  form.setRiesgoLesiones(entity.getRiesgoLesiones());
  form.setNecesidades(entity.getNecesidades());
  form.setObservaciones(entity.getObservaciones());
  return form;
 }
 protected Long version(ValoracionForm form) { return form.getVersion(); }
 protected String motivo(ValoracionForm form) { return form.getMotivoCambio(); }
}
