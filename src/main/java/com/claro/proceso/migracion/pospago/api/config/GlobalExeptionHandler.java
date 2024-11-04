package com.claro.proceso.migracion.pospago.api.config;

import com.claro.proceso.migracion.pospago.api.models.responses.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExeptionHandler {

    @ExceptionHandler(value = ErrorWebServiceConnectionRefusedException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ErrorWebServiceConnectionRefusedException ex) {
        log.error(HttpStatus.REQUEST_TIMEOUT + ex.getMessage());
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(new ErrorResponse(true,HttpStatus.REQUEST_TIMEOUT.value(), ex.getMessage()));
    }
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error(HttpStatus.NOT_FOUND + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(true,HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = ResourceNotValidException.class)
    public ResponseEntity<Object> handleResourceNotValidException(ResourceNotValidException ex) {
        log.error(HttpStatus.CHECKPOINT + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(true,HttpStatus.CHECKPOINT.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = SupplierAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleSupplierAlreadyExistsException(SupplierAlreadyExistsException ex) {
        log.error(HttpStatus.CONFLICT + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(true,HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = ParameterAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleParameterAlreadyExistsException(ParameterAlreadyExistsException ex) {
        log.error(HttpStatus.CONFLICT + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(true,HttpStatus.CONFLICT.value(), ex.getMessage()));
    }
    @ExceptionHandler(value = OrderAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleOrderAlreadyExistsException(OrderAlreadyExistsException ex) {
        log.error(HttpStatus.CONFLICT + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(true,HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = WebServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceNotRespondingException(WebServiceException ex) {
        log.error(HttpStatus.REQUEST_TIMEOUT + ex.getMessage());
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(new ErrorResponse(true,HttpStatus.REQUEST_TIMEOUT.value(), ex.getMessage()));
    }
    @ExceptionHandler(value = EncodingNotSupportedExcpetion.class)
    public ResponseEntity<ErrorResponse> handleEncodingNotSupportedException(EncodingNotSupportedExcpetion ex) {
        log.error(HttpStatus.EXPECTATION_FAILED + ex.getMessage());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ErrorResponse(true,HttpStatus.EXPECTATION_FAILED.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.error(HttpStatus.BAD_REQUEST + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(true,HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(HttpStatus.BAD_REQUEST + ex.getMessage());
        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessages.add(violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(true,HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error(HttpStatus.METHOD_NOT_ALLOWED + ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(true,HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error(HttpStatus.METHOD_NOT_ALLOWED + ex.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(true,HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage()));
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex){
        log.error(HttpStatus.BAD_REQUEST + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(true,HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        log.error(HttpStatus.BAD_REQUEST + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(true,HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex){
        log.error(HttpStatus.NOT_FOUND + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(true,HttpStatus.NOT_FOUND.value(), ex.getRequestURL()));
    }
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex){
        log.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErrorResponse(true,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.getContentType().toString()));
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleCommonException(Exception ex){
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = FlowMigrationException.class)
    public ResponseEntity<ErrorResponse> hanleFlowMigrationException(FlowMigrationException ex){
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(true,HttpStatus.CONFLICT.value(), ex.getMessage()));
    }
}
