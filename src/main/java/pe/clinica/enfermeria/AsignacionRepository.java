package pe.clinica.enfermeria;


import org.springframework.data.jpa.repository.*;
import java.util.List;
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
 boolean existsByPacienteIdAndProfesionalId(Long pacienteId, Long profesionalId);
 @Query("select a from Asignacion a join fetch a.paciente join fetch a.profesional order by a.fecha desc, a.paciente.nombre")
 List<Asignacion> listar();
}
