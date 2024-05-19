package io.archiveservice.api.exception.handler;

import io.archiveservice.service.exception.ArchiveCreateException;
import io.archiveservice.service.exception.ArchiveInputValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "io.archiveservice.api")
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(ArchiveInputValidationException.class)
	@ResponseBody
	public ResponseEntity<Object> genericHandler(ArchiveInputValidationException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(ArchiveCreateException.class)
	@ResponseBody
	public ResponseEntity<Object> genericHandler(ArchiveCreateException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
