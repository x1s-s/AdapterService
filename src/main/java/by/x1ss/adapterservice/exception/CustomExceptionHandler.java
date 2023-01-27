package by.x1ss.adapterservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(SmevEcxeption.class)
    public ResponseEntity<Object> handleSmevException(SmevEcxeption e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
