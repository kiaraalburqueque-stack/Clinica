package pe.clinica.enfermeria;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
 Optional<Profesional> findByUsuario(String usuario);
}
