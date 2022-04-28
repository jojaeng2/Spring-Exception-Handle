package example.springexception.handler;

import example.springexception.dto.ErrorResponse;
import example.springexception.exception.NoSuchElementFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceExceptionHandler {

    @ExceptionHandler(NoSuchElementFoundException.class)
    protected ResponseEntity<?> handleNoSuchElementFoundException(NoSuchElementFoundException e) {
        final ErrorResponse errorResponse = new ErrorResponse("Item Not Found", e.getMessage());


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
