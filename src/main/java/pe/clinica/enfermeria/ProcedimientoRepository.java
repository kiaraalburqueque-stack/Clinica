package pe.clinica.enfermeria;


import org.springframework.data.jpa.repository.*;
public interface ProcedimientoRepository extends JpaRepository<Procedimiento,Long>, JpaSpecificationExecutor<Procedimiento> {}
