package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
@Service
public class SignoVitalService extends ClinicalService<SignoVital,SignoVitalForm> {
 public SignoVitalService(SignoVitalRepository repository, AccesoService acceso, com.fasterxml.jackson.databind.ObjectMapper mapper) { super(repository, repository, acceso, mapper); }
 protected SignoVital nuevo() { return new SignoVital(); }
 protected void copiar(SignoVitalForm form, SignoVital entity) {
  entity.setFechaHora(form.getFechaHora());
  entity.setTemperatura(form.getTemperatura());
  entity.setSistolica(form.getSistolica());
  entity.setDiastolica(form.getDiastolica());
  entity.setFrecuenciaCardiaca(form.getFrecuenciaCardiaca());
  entity.setFrecuenciaRespiratoria(form.getFrecuenciaRespiratoria());
  entity.setSaturacion(form.getSaturacion());
  entity.setPeso(form.getPeso());
  entity.setTalla(form.getTalla());
  entity.setDolor(form.getDolor());
  entity.setGlucosa(form.getGlucosa());
 }
 public SignoVitalForm formulario(SignoVital entity) {
  var form=new SignoVitalForm(); form.setFechaHora(entity.getFechaHora()); form.setVersion(entity.getVersion());
  form.setTemperatura(entity.getTemperatura());
  form.setSistolica(entity.getSistolica());
  form.setDiastolica(entity.getDiastolica());
  form.setFrecuenciaCardiaca(entity.getFrecuenciaCardiaca());
  form.setFrecuenciaRespiratoria(entity.getFrecuenciaRespiratoria());
  form.setSaturacion(entity.getSaturacion());
  form.setPeso(entity.getPeso());
  form.setTalla(entity.getTalla());
  form.setDolor(entity.getDolor());
  form.setGlucosa(entity.getGlucosa());
  return form;
 }
 protected Long version(SignoVitalForm form) { return form.getVersion(); }
 protected String motivo(SignoVitalForm form) { return form.getMotivoCambio(); }
}
