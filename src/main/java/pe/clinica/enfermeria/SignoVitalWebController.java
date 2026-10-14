package pe.clinica.enfermeria;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.time.LocalDate;
@Controller
@RequestMapping("/pacientes/{pacienteId}/signos-vitales")
@Transactional(readOnly=true)
public class SignoVitalWebController {
 private final SignoVitalService service; private final AccesoService acceso;
 public SignoVitalWebController(SignoVitalService service,AccesoService acceso) { this.service=service;this.acceso=acceso; }
 private void datos(Model model,Long pacienteId) { model.addAttribute("paciente",acceso.paciente(pacienteId));model.addAttribute("profesional",acceso.actual());model.addAttribute("titulo","Signos vitales");model.addAttribute("slug","signos-vitales");model.addAttribute("requisitos","RF-ENF-10 / RF-ENF-12"); }
 @GetMapping public String historial(@PathVariable Long pacienteId,@RequestParam(required=false) LocalDate desde,@RequestParam(required=false) LocalDate hasta,@RequestParam(defaultValue="0") int page,Model model) {
  datos(model,pacienteId);model.addAttribute("registros",service.historial(pacienteId,desde,hasta,null,page,20));model.addAttribute("desde",desde);model.addAttribute("hasta",hasta);return "signos-vitales/historial";
 }
 @GetMapping("/nuevo") public String nuevo(@PathVariable Long pacienteId,Model model) { datos(model,pacienteId);var form=new SignoVitalForm();form.setFechaHora(ClinicalService.ahora().withSecond(0).withNano(0));model.addAttribute("form",form);model.addAttribute("id",null);return "signos-vitales/formulario"; }
 @PostMapping @Transactional public String crear(@PathVariable Long pacienteId,@Valid @ModelAttribute("form") SignoVitalForm form,BindingResult errors,Model model,RedirectAttributes flash) {
  datos(model,pacienteId);if(errors.hasErrors()) return "signos-vitales/formulario";service.crear(pacienteId,form);flash.addFlashAttribute("mensaje","Registro guardado correctamente");return "redirect:/pacientes/"+pacienteId+"/signos-vitales";
 }
 @GetMapping("/{id}/editar") public String editar(@PathVariable Long pacienteId,@PathVariable Long id,Model model) {
  datos(model,pacienteId);var r=service.obtener(pacienteId,id);if(r.getAnulado()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Registro anulado");model.addAttribute("form",service.formulario(r));model.addAttribute("id",id);return "signos-vitales/formulario";
 }
 @PostMapping("/{id}") @Transactional public String actualizar(@PathVariable Long pacienteId,@PathVariable Long id,@Valid @ModelAttribute("form") SignoVitalForm form,BindingResult errors,Model model,RedirectAttributes flash) {
  datos(model,pacienteId);model.addAttribute("id",id);if(errors.hasErrors()) return "signos-vitales/formulario";
  try { service.actualizar(pacienteId,id,form); } catch(ResponseStatusException e) {errors.reject("correccion",e.getReason());return "signos-vitales/formulario";}
  flash.addFlashAttribute("mensaje","Corrección guardada con auditoría");return "redirect:/pacientes/"+pacienteId+"/signos-vitales";
 }
 @PostMapping("/{id}/anular") @Transactional public String anular(@PathVariable Long pacienteId,@PathVariable Long id,@RequestParam String motivo,RedirectAttributes flash) { service.anular(pacienteId,id,motivo);flash.addFlashAttribute("mensaje","Registro anulado; se conserva en el historial");return "redirect:/pacientes/"+pacienteId+"/signos-vitales"; }
}
