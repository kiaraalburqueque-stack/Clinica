package pe.clinica.enfermeria;


import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@Configuration
@EnableMethodSecurity
@EnableTransactionManagement(order=0)
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
 @Bean UserDetailsService usuarios(UsuarioRepository repo) {
  return username -> repo.findByUsername(username).map(u -> User.withUsername(u.getUsername()).password(u.getPasswordHash())
   .authorities(SecurityRules.authorities(u)).disabled(!SecurityRules.habilitado(u)).build()).orElseThrow(() -> new UsernameNotFoundException("Usuario inválido"));
 }
 @Bean @Order(1) SecurityFilterChain api(HttpSecurity http,UsuarioRepository usuarios) throws Exception {
  return http.securityMatcher("/api/**").authorizeHttpRequests(a -> a.requestMatchers("/api/admin/**").hasAuthority("ADMINISTRAR")
   .requestMatchers("/api/auditoria/**","/api/auditoria").hasAuthority("AUDITORIA").requestMatchers("/api/pacientes").hasAuthority("PACIENTES").anyRequest().authenticated())
   .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).csrf(c -> c.disable()).httpBasic(b -> {})
   .addFilterBefore(new AccountStateFilter(usuarios),AuthorizationFilter.class).build();
 }
 @Bean @Order(2) SecurityFilterChain web(HttpSecurity http,UsuarioRepository usuarios,CuentaService cuenta) throws Exception {
  return http.authorizeHttpRequests(a -> a.requestMatchers("/login","/css/**","/error").permitAll()
   .requestMatchers("/admin/**").hasAuthority("ADMINISTRAR").requestMatchers("/auditoria").hasAuthority("AUDITORIA")
   .requestMatchers("/pacientes/*/*/nuevo","/pacientes/*/*/*/editar").hasAuthority("ENFERMERIA")
   .requestMatchers("/pacientes","/pacientes/*/datos").hasAuthority("PACIENTES").anyRequest().authenticated())
   .formLogin(f -> f.loginPage("/login").successHandler((request,response,authentication) -> response.sendRedirect(request.getContextPath()+cuenta.destino())).permitAll())
   .logout(l -> l.logoutSuccessUrl("/login?logout"))
   .addFilterBefore(new AccountStateFilter(usuarios),AuthorizationFilter.class).build();
 }
}
