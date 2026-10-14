package pe.clinica.enfermeria;


import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
public class AccountStateFilter extends OncePerRequestFilter {
 private final UsuarioRepository usuarios;
 public AccountStateFilter(UsuarioRepository usuarios) {this.usuarios=usuarios;}
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
  var auth=SecurityContextHolder.getContext().getAuthentication();
  if(auth!=null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
   var u=usuarios.findByUsername(auth.getName()).orElse(null);
   if(u==null || !SecurityRules.habilitado(u)) {
    SecurityContextHolder.clearContext();var session=request.getSession(false);if(session!=null) session.invalidate();
    if(request.getRequestURI().startsWith("/api/")) {response.setStatus(401);response.setContentType("application/json");response.getWriter().write("{\"mensaje\":\"Cuenta o roles desactivados\"}");}
    else response.sendRedirect(request.getContextPath()+"/login?error");return;
   }
   var refreshed=new UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials(),SecurityRules.authorities(u));
   refreshed.setDetails(auth.getDetails());SecurityContextHolder.getContext().setAuthentication(refreshed);
  }
  chain.doFilter(request,response);
 }
}
