package pe.clinica.enfermeria;


import java.lang.annotation.*;
@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {String accion();}
