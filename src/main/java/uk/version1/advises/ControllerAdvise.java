package uk.version1.advises;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvise {
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFound(final EntityNotFoundException exception){
        log.error("INVALID_RESPONSE: {}", exception.getLocalizedMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\""+ exception.getMessage()+"\"}");

    }


}
