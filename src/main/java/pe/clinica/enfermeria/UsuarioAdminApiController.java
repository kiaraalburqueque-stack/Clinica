package pe.clinica.enfermeria;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import jakarta.validation.Valid;
import java.util.*;
import java.net.URI;
@RestController @RequestMapping("/api/admin/usuarios")
public class UsuarioAdminApiController {
 private final AdminService service;
 public UsuarioAdminApiController(AdminService service) {this.service=service;}
 @GetMapping public List<Map<String,Object>> listar() {return service.usuarios().stream().map(AdminDto::usuario).toList();}
 @GetMapping("/{id}") public Map<String,Object> obtener(@PathVariable Long id) {return AdminDto.usuario(service.usuario(id));}
 @PostMapping public ResponseEntity<?> crear(@Valid @RequestBody UsuarioForm form) {var e=service.crearUsuario(form);return ResponseEntity.created(URI.create("/api/admin/usuarios/"+e.getId())).body(AdminDto.usuario(e));}
 @PutMapping("/{id}") public Map<String,Object> editar(@PathVariable Long id,@Valid @RequestBody UsuarioForm form) {return AdminDto.usuario(service.editarUsuario(id,form));}
 @PatchMapping("/{id}/estado") public Map<String,Object> estado(@PathVariable Long id,@Valid @RequestBody EstadoForm form) {return AdminDto.usuario(service.estadoUsuario(id,form.activo(),form.version()));}
 @DeleteMapping("/{id}") public ResponseEntity<Void> desactivar(@PathVariable Long id,@RequestParam Long version) {service.desactivarUsuario(id,version);return ResponseEntity.noContent().build();}
}
