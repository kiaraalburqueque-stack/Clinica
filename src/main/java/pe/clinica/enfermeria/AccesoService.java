package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
public class AccesoService {
 private final ProfesionalRepository profesionales;
 private final PacienteRepository pacientes;
 private final AsignacionRepository asignaciones;
 public AccesoService(ProfesionalRepository profesionales, PacienteRepository pacientes, AsignacionRepository asignaciones) {
  this.profesionales=profesionales; this.pacientes=pacientes; this.asignaciones=asignaciones;
 }
 public Profesional actual() {
  var auth=SecurityContextHolder.getContext().getAuthentication();
  return profesionales.findByUsuario(auth.getName()).orElseThrow(() -> new AccessDeniedException("Profesional no autorizado"));
 }
 public Paciente paciente(Long id) {
  var profesional=actual();
  if (!"SUPERVISOR".equals(profesional.getRol()) && !asignaciones.existsByPacienteIdAndProfesionalId(id,profesional.getId()))
   throw new AccessDeniedException("No tiene autorización para este paciente");
  return pacientes.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Paciente inexistente"));
 }
}
