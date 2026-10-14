package pe.clinica.enfermeria;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
 Optional<Usuario> findByUsername(String username);
 boolean existsByCorreoAndIdNot(String correo,Long id);
 boolean existsByUsernameAndIdNot(String username,Long id);
}
