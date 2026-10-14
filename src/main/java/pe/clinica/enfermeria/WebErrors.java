package pe.clinica.enfermeria;


import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.*;
@ControllerAdvice(annotations=Controller.class)
public class WebErrors {
 @ExceptionHandler(ResponseStatusException.class) public ModelAndView status(ResponseStatusException e) {return view(e.getStatusCode().value(),e.getReason());}
 @ExceptionHandler({DataIntegrityViolationException.class,OptimisticLockingFailureException.class}) public ModelAndView conflict(Exception e) {return view(409,"El registro cambió o entra en conflicto con datos existentes. Recarga la página.");}
 private ModelAndView view(int status,String message) {var m=new ModelAndView("error");m.addObject("status",status);m.addObject("detalle",message);m.setStatus(HttpStatus.valueOf(status));return m;}
}
