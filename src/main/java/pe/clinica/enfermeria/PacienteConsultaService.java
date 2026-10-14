package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;
@Service @Transactional(readOnly=true) @PreAuthorize("hasAuthority('PACIENTES')")
public class PacienteConsultaService {
 private final PacienteRepository repository;
 public PacienteConsultaService(PacienteRepository repository) {this.repository=repository;}
 public List<Paciente> listar(String buscar) {return repository.findAll().stream().filter(p -> buscar==null || buscar.isBlank() || (p.getNombre()+" "+p.getCodigo()+" "+p.getDocumento()).toLowerCase(Locale.ROOT).contains(buscar.toLowerCase(Locale.ROOT))).toList();}
 public Paciente obtener(Long id) {return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Paciente inexistente"));}
 public Map<String,Object> dto(Paciente p) {Map<String,Object> m=new LinkedHashMap<>();m.put("id",p.getId());m.put("codigo",p.getCodigo());m.put("nombre",p.getNombre());m.put("documento",p.getDocumento());m.put("sexo",p.getSexo());m.put("nacimiento",p.getNacimiento());return m;}
}
