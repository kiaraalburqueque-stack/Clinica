package pe.clinica.enfermeria;


import org.springframework.data.jpa.repository.*;
public interface SignoVitalRepository extends JpaRepository<SignoVital,Long>, JpaSpecificationExecutor<SignoVital> {}
