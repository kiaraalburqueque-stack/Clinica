package pe.clinica.enfermeria;


import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.time.LocalDate;
@RestController
public class AuditoriaApiController {
 private final AuditoriaService service;
 public AuditoriaApiController(AuditoriaService service) {this.service=service;}
 @GetMapping("/api/auditoria") public Page<Auditoria> listar(@RequestParam(required=false) String usuario,@RequestParam(required=false) String entidad,@RequestParam(required=false) String accion,@RequestParam(required=false) LocalDate desde,@RequestParam(required=false) LocalDate hasta,@RequestParam(defaultValue="0") int page) {return service.listar(usuario,entidad,accion,desde,hasta,page);}
}
