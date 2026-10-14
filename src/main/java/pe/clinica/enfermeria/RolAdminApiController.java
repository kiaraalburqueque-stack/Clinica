package pe.clinica.enfermeria;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import jakarta.validation.Valid;
import java.util.*;
import java.net.URI;
@RestController @RequestMapping("/api/admin/roles")
public class RolAdminApiController {
 private final AdminService service;
 public RolAdminApiController(AdminService service) {this.service=service;}
 @GetMapping public List<Map<String,Object>> listar() {return service.roles().stream().map(AdminDto::rol).toList();}
 @GetMapping("/{id}") public Map<String,Object> obtener(@PathVariable Long id) {return AdminDto.rol(service.rol(id));}
 @PostMapping public ResponseEntity<?> crear(@Valid @RequestBody RolForm form) {var e=service.crearRol(form);return ResponseEntity.created(URI.create("/api/admin/roles/"+e.getId())).body(AdminDto.rol(e));}
 @PutMapping("/{id}") public Map<String,Object> editar(@PathVariable Long id,@Valid @RequestBody RolForm form) {return AdminDto.rol(service.editarRol(id,form));}
 @PatchMapping("/{id}/estado") public Map<String,Object> estado(@PathVariable Long id,@Valid @RequestBody EstadoForm form) {return AdminDto.rol(service.estadoRol(id,form.activo(),form.version()));}
 @DeleteMapping("/{id}") public ResponseEntity<Void> desactivar(@PathVariable Long id,@RequestParam Long version) {service.desactivarRol(id,version);return ResponseEntity.noContent().build();}
}
