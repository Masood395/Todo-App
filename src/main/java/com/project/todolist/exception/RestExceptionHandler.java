package com.project.todolist.exception;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.project.todolist.controller.TodoController;
import com.project.todolist.model.TodoStatus;
import com.project.todolist.payloads.ApiResponse;

@RestControllerAdvice
public class RestExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(TodoController.class);


	@ExceptionHandler(TodoNotFoundException.class)
	public ResponseEntity<ApiResponse<?>> handleTodoNotFound(TodoNotFoundException ex) {
		log.error(ex.getMessage());
		ApiResponse<?> errordetails = new ApiResponse(false,ex.getMessage());
		return new ResponseEntity<>(errordetails, HttpStatus.NOT_FOUND);
	}
	
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
	        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
	                .stream()
	                .collect(Collectors.toMap(
	                        err -> err.getField(),
	                        err -> err.getDefaultMessage(),
	                        (existing, replacement) -> existing 
	                )); 

			ApiResponse<?> errordetails = new ApiResponse(false,errors);
	        return new ResponseEntity<>(errordetails, HttpStatus.BAD_REQUEST);
	    }
	
		@ExceptionHandler(MethodArgumentTypeMismatchException.class)
		public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
			log.error("Parameter type mismatch: {}", ex.getMessage());
			String message;
			if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
				Object[] enumConstants = ex.getRequiredType().getEnumConstants();
				message = String.format("Invalid value '%s' for parameter '%s'. Allowed values are: %s", ex.getValue(),
						ex.getName(), Arrays.toString(enumConstants));
			} else {
				message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s", ex.getValue(),
						ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown");
			}
			ApiResponse<?> errorDetails = new ApiResponse<>(false, message);
			return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
		}

	
		@ExceptionHandler(HttpMessageNotReadableException.class)
		public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
			log.error(ex.getMessage());
			String allowedStatuses = Arrays.stream(TodoStatus.values()).map(Enum::name).collect(Collectors.joining(", "));
			ApiResponse<?> errorDetails = new ApiResponse( false,
					"Invalid status. Allowed: "+allowedStatuses/* +ex.getMessage() */);
			return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
		}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalExc(Exception e) {
		log.error(e.getMessage());
		ApiResponse<?> errordetails = new ApiResponse(false, e.getMessage());
		return new ResponseEntity<>(errordetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	

}
