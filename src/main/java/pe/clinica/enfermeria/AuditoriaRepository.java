package pe.clinica.enfermeria;

import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditoriaRepository extends JpaRepository<Auditoria,Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Auditoria> {}
