package pe.clinica.enfermeria;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;

@Configuration
public class ValidationConfig {
 @Bean ValidationConfigurationCustomizer relojValidacion() {
  return config -> config.clockProvider(() -> Clock.system(ZoneId.of("America/Lima")));
 }
}
