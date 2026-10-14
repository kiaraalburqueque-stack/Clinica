package pe.clinica.enfermeria;


import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
public class PacienteConsultaApiController {
 private final PacienteConsultaService service;
 public PacienteConsultaApiController(PacienteConsultaService service) {this.service=service;}
 @GetMapping("/api/pacientes") public List<Map<String,Object>> listar(@RequestParam(required=false) String buscar) {return service.listar(buscar).stream().map(service::dto).toList();}
}
