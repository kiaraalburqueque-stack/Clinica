package pe.clinica.enfermeria;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
@Controller @RequestMapping("/admin/roles")
public class RolAdminWebController {
 private final AdminService service;
 public RolAdminWebController(AdminService service) {this.service=service;}
 private void catalogos(Model model) {model.addAttribute("permisos",Permiso.values());}
 @GetMapping public String listar(Model model) {model.addAttribute("elementos",service.roles());return "admin/roles/listado";}
 @GetMapping("/nuevo") public String nuevo(Model model) {catalogos(model);model.addAttribute("form",new RolForm());return "admin/roles/formulario";}
 @GetMapping("/{id}/editar") public String editar(@PathVariable Long id,Model model) {catalogos(model);model.addAttribute("form",service.formulario(service.rol(id)));model.addAttribute("id",id);return "admin/roles/formulario";}
 @PostMapping public String crear(@Valid @ModelAttribute("form") RolForm form,BindingResult errors,Model model,RedirectAttributes flash) {return guardar(null,form,errors,model,flash);}
 @PostMapping("/{id}") public String editar(@PathVariable Long id,@Valid @ModelAttribute("form") RolForm form,BindingResult errors,Model model,RedirectAttributes flash) {return guardar(id,form,errors,model,flash);}
 private String guardar(Long id,RolForm form,BindingResult errors,Model model,RedirectAttributes flash) {
  catalogos(model);model.addAttribute("id",id);if(errors.hasErrors()) return "admin/roles/formulario";
  try {if(id==null) service.crearRol(form);else service.editarRol(id,form);}
  catch(ResponseStatusException e) {errors.reject("guardado",e.getReason());return "admin/roles/formulario";}
  flash.addFlashAttribute("mensaje","Cambios guardados y auditados");return "redirect:/admin/roles";
 }
 @PostMapping("/{id}/estado") public String estado(@PathVariable Long id,@RequestParam boolean activo,@RequestParam Long version,RedirectAttributes flash) {
  try {service.estadoRol(id,activo,version);flash.addFlashAttribute("mensaje","Estado actualizado");} catch(ResponseStatusException e) {flash.addFlashAttribute("error",e.getReason());}
  return "redirect:/admin/roles";
 }
}
