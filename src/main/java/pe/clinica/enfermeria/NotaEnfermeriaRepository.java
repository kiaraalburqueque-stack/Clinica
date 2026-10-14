package pe.clinica.enfermeria;


import org.springframework.data.jpa.repository.*;
public interface NotaEnfermeriaRepository extends JpaRepository<NotaEnfermeria,Long>, JpaSpecificationExecutor<NotaEnfermeria> {}
