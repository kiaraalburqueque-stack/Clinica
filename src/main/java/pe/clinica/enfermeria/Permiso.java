package pe.clinica.enfermeria;


public enum Permiso {
 ADMINISTRAR("Administrar usuarios y roles"),
 AUDITORIA("Consultar la bitácora de auditoría"),
 ENFERMERIA("Registrar y corregir cuidados de enfermería"),
 CONSULTA_CLINICA("Consultar fichas e historiales clínicos"),
 PACIENTES("Consultar el listado y datos personales de pacientes"),
 SUPERVISION("Consultar pacientes asignados a otros profesionales");
 private final String etiqueta;
 Permiso(String etiqueta) {this.etiqueta=etiqueta;}
 public String getEtiqueta() {return etiqueta;}
}
