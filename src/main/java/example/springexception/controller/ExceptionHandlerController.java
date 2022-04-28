package example.springexception.controller;

import example.springexception.exception.NoSuchElementFoundException;
import example.springexception.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ExceptionHandlerController {

    private final ProductService productService;

    @GetMapping("/product/exceptionhandler/{id}")
    public ResponseEntity getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @ExceptionHandler(NoSuchElementFoundException.class)
    public ResponseEntity<String> handleNoSuchElementFoundException(NoSuchElementFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}
