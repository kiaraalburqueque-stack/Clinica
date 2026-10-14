package pe.clinica.enfermeria;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.*;
@RestControllerAdvice(annotations=RestController.class)
@org.springframework.core.annotation.Order(0)
public class ApiErrors {
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<?> invalid(MethodArgumentNotValidException e) {
  var errores=new LinkedHashMap<String,String>();e.getBindingResult().getAllErrors().forEach(error -> errores.put(error instanceof org.springframework.validation.FieldError f?f.getField():error.getObjectName(),error.getDefaultMessage()));return ResponseEntity.badRequest().body(Map.of("mensaje","Datos inválidos","errores",errores));
 }
 @ExceptionHandler(AccessDeniedException.class) public ResponseEntity<?> denied(AccessDeniedException e) {return ResponseEntity.status(403).body(Map.of("mensaje",e.getMessage()));}
 @ExceptionHandler(ResponseStatusException.class) public ResponseEntity<?> status(ResponseStatusException e) {return ResponseEntity.status(e.getStatusCode()).body(Map.of("mensaje",Objects.requireNonNullElse(e.getReason(),"Solicitud inválida")));}
 @ExceptionHandler({HttpMessageNotReadableException.class,MethodArgumentTypeMismatchException.class,org.springframework.web.bind.MissingServletRequestParameterException.class}) public ResponseEntity<?> malformed(Exception e) {return ResponseEntity.badRequest().body(Map.of("mensaje","Formato de datos inválido o parámetro obligatorio ausente"));}
 @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class) public ResponseEntity<?> integridad(Exception e) {return ResponseEntity.status(409).body(Map.of("mensaje","Conflicto de integridad: el registro está duplicado o relacionado"));}
 @ExceptionHandler(jakarta.validation.ConstraintViolationException.class) public ResponseEntity<?> constraint(Exception e) {return ResponseEntity.badRequest().body(Map.of("mensaje","Datos inválidos"));}
 @ExceptionHandler(OptimisticLockingFailureException.class) public ResponseEntity<?> conflict(Exception e) {return ResponseEntity.status(409).body(Map.of("mensaje","El registro cambió. Recargue antes de corregirlo"));}
}
