package pe.clinica.enfermeria;


import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
@ControllerAdvice(annotations=Controller.class)
public class NavigationAdvice {
 private final CuentaService cuenta;
 public NavigationAdvice(CuentaService cuenta) {this.cuenta=cuenta;}
 @ModelAttribute public void navigation(Model model) {
  var auth=SecurityContextHolder.getContext().getAuthentication();
  if(auth==null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) return;
  var u=cuenta.actual();model.addAttribute("profesional",u.getProfesional());model.addAttribute("cuenta",u);
  for(Permiso p:Permiso.values()) model.addAttribute("puede"+p.name(),SecurityRules.permiso(u,p));
 }
}
