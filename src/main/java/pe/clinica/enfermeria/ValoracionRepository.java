package pe.clinica.enfermeria;


import org.springframework.data.jpa.repository.*;
public interface ValoracionRepository extends JpaRepository<Valoracion,Long>, JpaSpecificationExecutor<Valoracion> {}
