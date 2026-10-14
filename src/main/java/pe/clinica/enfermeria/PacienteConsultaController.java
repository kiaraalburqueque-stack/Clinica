package pe.clinica.enfermeria;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
@Controller
public class PacienteConsultaController {
 private final PacienteConsultaService service;
 public PacienteConsultaController(PacienteConsultaService service) {this.service=service;}
 @GetMapping("/pacientes") public String listar(@RequestParam(required=false) String buscar,Model model) {model.addAttribute("pacientes",service.listar(buscar));model.addAttribute("buscar",buscar);return "pacientes/listado";}
 @GetMapping("/pacientes/{id}/datos") public String datos(@PathVariable Long id,Model model) {model.addAttribute("paciente",service.obtener(id));return "pacientes/datos";}
 @GetMapping("/cuenta") public String cuenta() {return "cuenta";}
}
