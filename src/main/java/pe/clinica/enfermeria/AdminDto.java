package pe.clinica.enfermeria;


import java.util.*;
public final class AdminDto {
 private AdminDto() {}
 public static Map<String,Object> usuario(Usuario u) {
  Map<String,Object> m=new LinkedHashMap<>();m.put("id",u.getId());m.put("version",u.getVersion());m.put("username",u.getUsername());m.put("nombre",u.getNombre());m.put("correo",u.getCorreo());m.put("activo",u.getActivo());m.put("profesionalId",u.getProfesional().getId());m.put("roles",u.getRoles().stream().map(AdminDto::rol).toList());return m;
 }
 public static Map<String,Object> rol(Rol r) {
  Map<String,Object> m=new LinkedHashMap<>();m.put("id",r.getId());m.put("version",r.getVersion());m.put("codigo",r.getCodigo());m.put("nombre",r.getNombre());m.put("descripcion",r.getDescripcion());m.put("activo",r.getActivo());m.put("permisos",r.getPermisos());return m;
 }
}
