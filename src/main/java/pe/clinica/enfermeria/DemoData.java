package pe.clinica.enfermeria;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.*;
@Component
@org.springframework.core.annotation.Order(10)
@ConditionalOnProperty(name="app.seed-demo",havingValue="true")
public class DemoData implements CommandLineRunner {
 private final ProfesionalRepository profesionales;
 private final PacienteRepository pacientes;
 private final AsignacionRepository asignaciones;
 private final PasswordEncoder encoder;
 public DemoData(ProfesionalRepository profesionales, PacienteRepository pacientes, AsignacionRepository asignaciones, PasswordEncoder encoder) {
  this.profesionales=profesionales;this.pacientes=pacientes;this.asignaciones=asignaciones;this.encoder=encoder;
 }
 @Transactional public void run(String... args) {
  if (profesionales.count()!=0) return;
  var ana=profesional("ana","Ana Torres","ENFERMERIA");
  var elena=profesional("elena","Elena Ramos","ENFERMERIA");
  profesional("supervisor","Supervisión de enfermería","SUPERVISOR");
  var juan=paciente("PAC-001","DEMO0001","Juan Pérez López","Masculino",LocalDate.of(1985,3,12));
  var maria=paciente("PAC-002","DEMO0002","María García Ruiz","Femenino",LocalDate.of(1993,7,8));
  var luis=paciente("PAC-003","DEMO0003","Luis Mendoza Díaz","Masculino",LocalDate.of(1978,11,20));
  var hoy=LocalDate.now(ZoneId.of("America/Lima"));
  for (int i=0;i<14;i++) {
   asignar(ana,juan,hoy.plusDays(i),"Mañana","Medicina interna","201","A");
   asignar(ana,maria,hoy.plusDays(i),"Mañana","Medicina interna","201","B");
   asignar(elena,luis,hoy.plusDays(i),"Tarde","Cirugía","202","A");
  }
 }
 private Profesional profesional(String usuario,String nombre,String rol) {
  var p=new Profesional();p.setUsuario(usuario);p.setNombre(nombre);p.setRol(rol);p.setPasswordHash(encoder.encode("Demo123!"));return profesionales.save(p);
 }
 private Paciente paciente(String codigo,String documento,String nombre,String sexo,LocalDate nacimiento) {
  var p=new Paciente();p.setCodigo(codigo);p.setDocumento(documento);p.setNombre(nombre);p.setSexo(sexo);p.setNacimiento(nacimiento);return pacientes.save(p);
 }
 private void asignar(Profesional profesional,Paciente paciente,LocalDate fecha,String turno,String servicio,String habitacion,String cama) {
  var a=new Asignacion();a.setProfesional(profesional);a.setPaciente(paciente);a.setFecha(fecha);a.setTurno(turno);a.setServicio(servicio);a.setHabitacion(habitacion);a.setCama(cama);asignaciones.save(a);
 }
}
