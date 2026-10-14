package pe.clinica.enfermeria;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.*;
import java.util.*;
@Service @Transactional(readOnly=true) @PreAuthorize("hasAuthority('AUDITORIA')")
public class AuditoriaService {
 private final AuditoriaRepository repository;
 public AuditoriaService(AuditoriaRepository repository) {this.repository=repository;}
 public Page<Auditoria> listar(String usuario,String entidad,String accion,LocalDate desde,LocalDate hasta,int page) {
  if(page<0 || (desde!=null && hasta!=null && desde.isAfter(hasta))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Filtros inválidos");
  Specification<Auditoria> spec=(r,q,b) -> {
   var predicates=new ArrayList<jakarta.persistence.criteria.Predicate>();
   if(usuario!=null && !usuario.isBlank()) predicates.add(b.equal(r.get("usuario"),usuario));
   if(entidad!=null && !entidad.isBlank()) predicates.add(b.equal(r.get("tipo"),entidad));
   if(accion!=null && !accion.isBlank()) predicates.add(b.equal(r.get("accion"),accion));
   if(desde!=null) predicates.add(b.greaterThanOrEqualTo(r.get("fechaHora"),desde.atStartOfDay()));
   if(hasta!=null) predicates.add(b.lessThan(r.get("fechaHora"),hasta.plusDays(1).atStartOfDay()));
   return b.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
  };
  return repository.findAll(spec,PageRequest.of(page,20,Sort.by(Sort.Order.desc("fechaHora"),Sort.Order.desc("id"))));
 }
}
