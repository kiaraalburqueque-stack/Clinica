package pe.clinica.enfermeria;


import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;
@RestController
@RequestMapping("/api/enfermeria/pacientes-asignados")
public class AsignacionApiController {
 private final AsignacionService service;
 public AsignacionApiController(AsignacionService service) {this.service=service;}
 @GetMapping public List<Map<String,Object>> listar(@RequestParam(required=false) LocalDate fecha,@RequestParam(required=false) String turno,@RequestParam(required=false) String servicio,@RequestParam(required=false) Long profesionalId,@RequestParam(required=false) String buscar) {
  if(fecha==null) fecha=LocalDate.now(ZoneId.of("America/Lima"));return service.listar(fecha,turno,servicio,profesionalId,buscar);
 }
}
