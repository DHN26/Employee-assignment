package io.vcti.demo.exceptionHandling;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class MyExceptionHandler {
	
	@ExceptionHandler({ EntityNotFoundException.class })//no need to specify
	public ResponseEntity<?> exceptionMethod(EntityNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<?> generalException()
	{
		return ResponseEntity.internalServerError().body("Some error occured!");
	}
	
	@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("You do not have permission to access this resource.");
    }
	
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Authentication is required to access this resource.");
    }

}
