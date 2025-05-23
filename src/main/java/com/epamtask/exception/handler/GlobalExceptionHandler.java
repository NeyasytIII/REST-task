package com.epamtask.exception.handler;

import com.epamtask.dto.errordto.ErrorResponseDto;
import com.epamtask.dto.errordto.ValidationErrorDto;
import com.epamtask.exception.EntityAlreadyExistsException;
import com.epamtask.exception.InvalidCredentialsException;
import com.epamtask.mapper.ErrorMapper;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ErrorMapper errorMapper;

    public GlobalExceptionHandler(ErrorMapper errorMapper) {
        this.errorMapper = errorMapper;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDto handleValidation(MethodArgumentNotValidException ex) {
        log.error("Validation failed", ex);
        return errorMapper.toValidationDto(ex.getBindingResult().getFieldErrors());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDto handleBind(BindException ex) {
        log.error("Bind exception", ex);
        return errorMapper.toValidationDto(ex.getFieldErrors());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDto handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Constraint violation", ex);
        ValidationErrorDto dto = new ValidationErrorDto();
        dto.setErrors(ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.toList()));
        return dto;
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityAlreadyExists(EntityAlreadyExistsException ex) {
        log.error("Entity already exists", ex);
        ErrorResponseDto dto = buildErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                List.of("Entity already exists")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Illegal argument", ex);
        ErrorResponseDto dto = buildErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                List.of("Illegal argument exception")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(com.epamtask.exception.NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(com.epamtask.exception.NotFoundException ex) {
        log.error("Not found", ex);
        ErrorResponseDto dto = buildErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                List.of("Entity not found")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
        log.error("Unexpected exception", ex);
        ErrorResponseDto dto = buildErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                List.of("Unexpected error occurred")
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    private ErrorResponseDto buildErrorResponse(String exception, String message, List<String> details) {
        ErrorResponseDto dto = new ErrorResponseDto();
        dto.setException(exception);
        dto.setMessage(message);
        dto.setTimestamp(LocalDateTime.now());
        dto.setDetails(details);
        return dto;
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCredentials(InvalidCredentialsException ex) {
        log.error("Invalid credentials", ex);
        ErrorResponseDto dto = buildErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                List.of("Authentication failed")
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(dto);
    }
}