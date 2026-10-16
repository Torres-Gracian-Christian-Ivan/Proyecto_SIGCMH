package edu.unam.common.exception;

import edu.unam.common.response.ErrorDetail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    private String getMessage(String key) {
        return messageSource.getMessage(
                key,
                null,
                key,
                LocaleContextHolder.getLocale()
        );
    }

    private ResponseEntity<ErrorDetail> error(
            HttpStatus status,
            String code,
            String title,
            String detail,
            Map<String, Object> source
    ) {

        ErrorDetail errorDetail = new ErrorDetail(
                status.value(),
                code,
                title,
                detail,
                source
        );

        return ResponseEntity.status(status).body(errorDetail);
    }

    private ResponseEntity<ErrorDetail> error(
            HttpStatus status,
            String code,
            String title,
            String detail
    ) {
        return error(status, code, title, detail, null);
    }

    //====================================================
    // 404
    //====================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetail> handleNotFound(
            ResourceNotFoundException ex) {

        return error(
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND",
                "Recurso no encontrado",
                getMessage(ex.getMessage())
        );
    }

    //====================================================
    // 409
    //====================================================

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDetail> handleDuplicate(
            DuplicateResourceException ex) {

        return error(
                HttpStatus.CONFLICT,
                "RESOURCE_ALREADY_EXISTS",
                "Conflicto de integridad",
                getMessage(ex.getMessage())
        );
    }

    //====================================================
    // 400
    //====================================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetail> handleIllegalArgument(
            IllegalArgumentException ex) {

        return error(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                "Argumento inválido",
                getMessage(ex.getMessage())
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetail> handleJsonError(
            HttpMessageNotReadableException ex) {

        return error(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                "JSON inválido",
                "El cuerpo de la petición tiene un formato incorrecto."
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, Object> fields =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage,
                                (a,b)->a,
                                LinkedHashMap::new
                        ));

        return error(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Error de validación",
                "Uno o más campos son inválidos.",
                fields
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetail> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return error(
                HttpStatus.BAD_REQUEST,
                "TYPE_MISMATCH",
                "Tipo de dato incorrecto",
                "El parámetro '" + ex.getName() + "' tiene un formato inválido.",
                Map.of(
                        "parameter", ex.getName(),
                        "value", String.valueOf(ex.getValue())
                )
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetail> handleConstraint(
            ConstraintViolationException ex) {

        Map<String,Object> errores =
                ex.getConstraintViolations()
                        .stream()
                        .collect(Collectors.toMap(
                                v -> v.getPropertyPath().toString(),
                                ConstraintViolation::getMessage
                        ));

        return error(
                HttpStatus.BAD_REQUEST,
                "CONSTRAINT_VIOLATION",
                "Restricción inválida",
                "Uno o más parámetros son inválidos.",
                errores
        );
    }

    //====================================================
    // 500
    //====================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleException(
            Exception ex) {

        return error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Error interno del servidor",
                ex.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetail> handleIntegrity(
            DataIntegrityViolationException ex) {

        return error(
                HttpStatus.CONFLICT,
                "DATA_INTEGRITY_ERROR",
                "Conflicto de integridad",
                "No es posible eliminar el recurso porque tiene información relacionada."
        );
    }

}