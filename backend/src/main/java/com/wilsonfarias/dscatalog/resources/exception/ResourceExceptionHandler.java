package com.wilsonfarias.dscatalog.resources.exception;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wilsonfarias.dscatalog.services.exceptions.DatabaseException;
import com.wilsonfarias.dscatalog.services.exceptions.ResourceNotFoundException;

/* Classe que será responsável pelo controle de exceção que
 * ocorrer na cama de Resources (Controlador Rest) */

@ControllerAdvice
public class ResourceExceptionHandler {

	/* Sempre que ocorrer, em algum dos controladores REST essa exceção,
	 * ela será redirecionada para este método para ser tratada */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(httpStatus.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(httpStatus).body(err);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> dataBaseIntegrityError(DatabaseException e, HttpServletRequest request){
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(httpStatus.value());
		err.setError("Database exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(httpStatus).body(err);
	}
	
}
