package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDate;
import java.util.*;
@Service
public class AsignacionService {
 private final AsignacionRepository repository; private final AccesoService acceso;
 public AsignacionService(AsignacionRepository repository, AccesoService acceso) { this.repository=repository;this.acceso=acceso; }
 @Transactional(readOnly=true)
 public List<Map<String,Object>> listar(LocalDate fecha,String turno,String servicio,Long profesionalId,String buscar) {
  var actual=acceso.actual(); boolean supervisor="SUPERVISOR".equals(actual.getRol());
  if (profesionalId!=null && !profesionalId.equals(actual.getId()) && !supervisor) throw new AccessDeniedException("Asignaciones no autorizadas");
  var seleccionado=profesionalId!=null?profesionalId:actual.getId();
  return repository.listar().stream().filter(a -> a.getProfesional().getId().equals(seleccionado))
   .filter(a -> fecha==null || a.getFecha().equals(fecha))
   .filter(a -> turno==null || turno.isBlank() || a.getTurno().equals(turno))
   .filter(a -> servicio==null || servicio.isBlank() || a.getServicio().equals(servicio))
   .filter(a -> buscar==null || buscar.isBlank() || (a.getPaciente().getNombre()+" "+a.getPaciente().getDocumento()+" "+a.getPaciente().getCodigo()).toLowerCase(Locale.ROOT).contains(buscar.toLowerCase(Locale.ROOT)))
   .map(a -> { Map<String,Object> m=new LinkedHashMap<>();m.put("id",a.getId());m.put("pacienteId",a.getPaciente().getId());m.put("paciente",a.getPaciente().getNombre());m.put("codigo",a.getPaciente().getCodigo());m.put("documento",a.getPaciente().getDocumento());m.put("profesional",a.getProfesional().getNombre());m.put("fecha",a.getFecha());m.put("turno",a.getTurno());m.put("servicio",a.getServicio());m.put("habitacion",a.getHabitacion());m.put("cama",a.getCama());return m; }).toList();
 }
}
