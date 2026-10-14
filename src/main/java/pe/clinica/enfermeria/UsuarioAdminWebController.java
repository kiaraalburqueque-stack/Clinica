package pe.clinica.enfermeria;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
@Controller @RequestMapping("/admin/usuarios")
public class UsuarioAdminWebController {
 private final AdminService service;
 public UsuarioAdminWebController(AdminService service) {this.service=service;}
 private void catalogos(Model model) {model.addAttribute("roles",service.roles().stream().filter(Rol::getActivo).toList());}
 @GetMapping public String listar(Model model) {model.addAttribute("elementos",service.usuarios());return "admin/usuarios/listado";}
 @GetMapping("/nuevo") public String nuevo(Model model) {catalogos(model);model.addAttribute("form",new UsuarioForm());return "admin/usuarios/formulario";}
 @GetMapping("/{id}/editar") public String editar(@PathVariable Long id,Model model) {catalogos(model);model.addAttribute("form",service.formulario(service.usuario(id)));model.addAttribute("id",id);return "admin/usuarios/formulario";}
 @PostMapping public String crear(@Valid @ModelAttribute("form") UsuarioForm form,BindingResult errors,Model model,RedirectAttributes flash) {return guardar(null,form,errors,model,flash);}
 @PostMapping("/{id}") public String editar(@PathVariable Long id,@Valid @ModelAttribute("form") UsuarioForm form,BindingResult errors,Model model,RedirectAttributes flash) {return guardar(id,form,errors,model,flash);}
 private String guardar(Long id,UsuarioForm form,BindingResult errors,Model model,RedirectAttributes flash) {
  catalogos(model);model.addAttribute("id",id);if(errors.hasErrors()) return "admin/usuarios/formulario";
  try {if(id==null) service.crearUsuario(form);else service.editarUsuario(id,form);}
  catch(ResponseStatusException e) {errors.reject("guardado",e.getReason());form.setPassword("");return "admin/usuarios/formulario";}
  flash.addFlashAttribute("mensaje","Cambios guardados y auditados");return "redirect:/admin/usuarios";
 }
 @PostMapping("/{id}/estado") public String estado(@PathVariable Long id,@RequestParam boolean activo,@RequestParam Long version,RedirectAttributes flash) {
  try {service.estadoUsuario(id,activo,version);flash.addFlashAttribute("mensaje","Estado actualizado");} catch(ResponseStatusException e) {flash.addFlashAttribute("error",e.getReason());}
  return "redirect:/admin/usuarios";
 }
}
