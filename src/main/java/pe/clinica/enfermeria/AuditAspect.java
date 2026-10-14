package pe.clinica.enfermeria;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.fasterxml.jackson.databind.ObjectMapper;
@Aspect @Component @Order(500)
public class AuditAspect {
 private final AuditoriaRepository auditoria;private final ObjectMapper mapper;
 public AuditAspect(AuditoriaRepository auditoria,ObjectMapper mapper) {this.auditoria=auditoria;this.mapper=mapper;}
 @Around("@annotation(operation)") public Object auditar(ProceedingJoinPoint point,Auditable operation) throws Throwable {
  if(!TransactionSynchronizationManager.isActualTransactionActive()) throw new IllegalStateException("La auditoría requiere una transacción");
  Object anterior=null;String motivo=null;var args=point.getArgs();
  if(!"CREAR".equals(operation.accion())) {
   if(point.getTarget() instanceof ClinicalService<?,?> clinical) {anterior=clinical.snapshotForAudit((Long)args[0],(Long)args[1]);motivo="ANULAR".equals(operation.accion())?(String)args[2]:clinical.motivoForAudit(args[2]);}
   else if(point.getTarget() instanceof AdminService admin) {anterior=point.getSignature().getName().contains("Usuario")?AdminDto.usuario(admin.usuario((Long)args[0])):AdminDto.rol(admin.rol((Long)args[0]));}
  }
  var result=point.proceed();
  if(!(result instanceof AuditTarget target)) throw new IllegalStateException("La operación no devolvió una entidad auditable");
  var a=new Auditoria();a.setTipo(org.hibernate.Hibernate.getClass(result).getSimpleName());a.setRegistroId(target.getId());a.setAccion(operation.accion());a.setUsuario(SecurityContextHolder.getContext().getAuthentication().getName());a.setFechaHora(ClinicalService.ahora());a.setMotivo(motivo);
  if(anterior!=null) a.setDatosAnteriores(anterior instanceof String s?s:mapper.writeValueAsString(anterior));auditoria.saveAndFlush(a);return result;
 }
}
