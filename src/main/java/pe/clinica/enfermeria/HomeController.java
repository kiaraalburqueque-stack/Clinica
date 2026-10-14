package pe.clinica.enfermeria;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
@Controller
@Transactional(readOnly=true)
public class HomeController {
 private final AsignacionService asignaciones;private final AccesoService acceso;private final ProfesionalRepository profesionales;
 public HomeController(AsignacionService asignaciones,AccesoService acceso,ProfesionalRepository profesionales) {this.asignaciones=asignaciones;this.acceso=acceso;this.profesionales=profesionales;}
 @GetMapping("/login") public String login() {return "login";}
 @GetMapping("/") public String inicio(@RequestParam(required=false) LocalDate fecha,@RequestParam(required=false) String turno,@RequestParam(required=false) String servicio,@RequestParam(required=false) Long profesionalId,@RequestParam(required=false) String buscar,Model model) {
  if(fecha==null) fecha=LocalDate.now(ZoneId.of("America/Lima"));
  var actual=acceso.actual();model.addAttribute("profesional",actual);model.addAttribute("asignaciones",asignaciones.listar(fecha,turno,servicio,profesionalId,buscar));model.addAttribute("fecha",fecha);model.addAttribute("turno",turno);model.addAttribute("servicio",servicio);model.addAttribute("buscar",buscar);model.addAttribute("profesionalId",profesionalId);
  if("SUPERVISOR".equals(actual.getRol())) model.addAttribute("profesionales",profesionales.findAll());
  return "inicio";
 }
 @GetMapping("/pacientes/{id}") public String paciente(@PathVariable Long id,Model model) {model.addAttribute("paciente",acceso.paciente(id));model.addAttribute("profesional",acceso.actual());return "paciente";}
}
