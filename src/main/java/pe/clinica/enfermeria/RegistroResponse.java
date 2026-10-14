package pe.clinica.enfermeria;


import java.util.*;
public final class RegistroResponse {
 private RegistroResponse() {}
 public static <T extends RegistroClinico,F> Map<String,Object> de(T r,ClinicalService<T,F> service) {
  Map<String,Object> m=new LinkedHashMap<>();m.put("id",r.getId());m.put("version",r.getVersion());m.put("pacienteId",r.getPaciente().getId());m.put("profesional",r.getProfesional().getNombre());m.put("fechaHora",r.getFechaHora());m.put("anulado",r.getAnulado());m.put("motivoAnulacion",r.getMotivoAnulacion());m.put("datos",service.formulario(r));return m;
 }
}
