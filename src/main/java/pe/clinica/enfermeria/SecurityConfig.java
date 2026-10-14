package pe.clinica.enfermeria;


import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
 @Bean UserDetailsService usuarios(ProfesionalRepository repo) {
  return username -> repo.findByUsuario(username).map(p -> User.withUsername(p.getUsuario()).password(p.getPasswordHash()).roles(p.getRol()).build()).orElseThrow(() -> new UsernameNotFoundException("Usuario inválido"));
 }
 @Bean @Order(1) SecurityFilterChain api(HttpSecurity http) throws Exception {
  // API independiente: solo HTTP Basic, sin autenticación por cookie ni sesión.
  return http.securityMatcher("/api/**").authorizeHttpRequests(a -> a.anyRequest().authenticated())
   .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .csrf(c -> c.disable()).httpBasic(b -> {}).build();
 }
 @Bean @Order(2) SecurityFilterChain web(HttpSecurity http) throws Exception {
  return http.authorizeHttpRequests(a -> a.requestMatchers("/login","/css/**","/error").permitAll().anyRequest().authenticated())
   .formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/",true).permitAll())
   .logout(l -> l.logoutSuccessUrl("/login?logout")).build();
 }
}
