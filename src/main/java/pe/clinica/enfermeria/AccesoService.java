package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
public class AccesoService {
 private final CuentaService cuenta;private final PacienteRepository pacientes;private final AsignacionRepository asignaciones;
 public AccesoService(CuentaService cuenta,PacienteRepository pacientes,AsignacionRepository asignaciones) {this.cuenta=cuenta;this.pacientes=pacientes;this.asignaciones=asignaciones;}
 public Profesional actual() {return cuenta.actual().getProfesional();}
 public Paciente paciente(Long id) {
  if(!cuenta.puede("CONSULTA_CLINICA")) throw new AccessDeniedException("No tiene permiso para consultar información clínica");
  boolean global=cuenta.puede("SUPERVISION") || cuenta.puede("PACIENTES");
  if(!global && !asignaciones.existsByPacienteIdAndProfesionalId(id,actual().getId())) throw new AccessDeniedException("No tiene autorización para este paciente");
  return pacientes.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Paciente inexistente"));
 }
}
