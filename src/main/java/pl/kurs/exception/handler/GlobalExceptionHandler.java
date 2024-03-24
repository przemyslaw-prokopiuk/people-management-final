package pl.kurs.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.exception.*;
import pl.kurs.exception.model.ExceptionDto;
import pl.kurs.exception.model.ValidationExceptionDto;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionDto handleEntityNotFoundException(RuntimeException exception) {
        return new ExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ValidationExceptionDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ValidationExceptionDto exceptionDto = new ValidationExceptionDto();
        exception.getFieldErrors().forEach(fieldError -> exceptionDto.addViolation(fieldError.getField(), fieldError.getDefaultMessage()));
        exception.getBindingResult().getGlobalErrors().forEach(globalError -> exceptionDto.addViolation(globalError.getObjectName(), globalError.getDefaultMessage()));
        return exceptionDto;
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    @ResponseStatus(CONFLICT)
    public ExceptionDto handleDataIntegrityViolationException(RuntimeException exception) {
        log.info("DataIntegrityViolationException: ", exception);
        return new ExceptionDto("Request could not be processed due to conflict with another resource.");
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionDto handleConstraintViolationException(jakarta.validation.ConstraintViolationException exception) {
        ValidationExceptionDto exceptionDto = new ValidationExceptionDto();
        exception.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            exceptionDto.addViolation(field, message);
        });
        return exceptionDto;
    }

    @ExceptionHandler({PositionOverlapException.class, HttpMessageNotReadableException.class, UploadInProgressException.class,
            IncorrectSearchCriteriaException.class, CsvProcessingException.class, CsvValidationException.class, UnsupportedTypeException.class})
    @ResponseStatus(BAD_REQUEST)
    public ExceptionDto handleBadRequestException(RuntimeException exception) {
        return new ExceptionDto(exception.getMessage());
    }

}
