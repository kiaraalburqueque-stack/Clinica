package pe.clinica.enfermeria;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.time.LocalDate;
@Controller
public class AuditoriaWebController {
 private final AuditoriaService service;
 public AuditoriaWebController(AuditoriaService service) {this.service=service;}
 @GetMapping("/auditoria") public String listar(@RequestParam(required=false) String usuario,@RequestParam(required=false) String entidad,@RequestParam(required=false) String accion,@RequestParam(required=false) LocalDate desde,@RequestParam(required=false) LocalDate hasta,@RequestParam(defaultValue="0") int page,Model model) {
  model.addAttribute("registros",service.listar(usuario,entidad,accion,desde,hasta,page));model.addAttribute("usuario",usuario);model.addAttribute("entidad",entidad);model.addAttribute("accion",accion);model.addAttribute("desde",desde);model.addAttribute("hasta",hasta);return "auditoria";
 }
}
