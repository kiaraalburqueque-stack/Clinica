package pe.clinica.enfermeria;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.net.URI;
import java.util.Map;
@RestController
@RequestMapping("/api/pacientes/{pacienteId}/signos-vitales")
@Transactional(readOnly=true)
public class SignoVitalApiController {
 private final SignoVitalService service;
 public SignoVitalApiController(SignoVitalService service) { this.service=service; }
 @GetMapping public Page<Map<String,Object>> historial(@PathVariable Long pacienteId,@RequestParam(required=false) LocalDate desde,@RequestParam(required=false) LocalDate hasta,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) {
  return service.historial(pacienteId,desde,hasta,null,page,size).map(r -> RegistroResponse.de(r,service));
 }
 @GetMapping("/{id}") public Map<String,Object> detalle(@PathVariable Long pacienteId,@PathVariable Long id) { return RegistroResponse.de(service.obtener(pacienteId,id),service); }
 @PostMapping @Transactional public ResponseEntity<Map<String,Object>> crear(@PathVariable Long pacienteId,@Valid @RequestBody SignoVitalForm form) {
  var r=service.crear(pacienteId,form);return ResponseEntity.created(URI.create("/api/pacientes/"+pacienteId+"/signos-vitales/"+r.getId())).body(RegistroResponse.de(r,service));
 }
 @PutMapping("/{id}") @Transactional public Map<String,Object> actualizar(@PathVariable Long pacienteId,@PathVariable Long id,@Valid @RequestBody SignoVitalForm form) { return RegistroResponse.de(service.actualizar(pacienteId,id,form),service); }
 @DeleteMapping("/{id}") @Transactional public ResponseEntity<Void> anular(@PathVariable Long pacienteId,@PathVariable Long id,@Valid @RequestBody AnulacionForm form) { service.anular(pacienteId,id,form.motivo());return ResponseEntity.noContent().build(); }
}
