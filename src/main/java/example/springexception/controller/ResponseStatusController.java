package example.springexception.controller;


import example.springexception.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.DispatcherServlet;

@Controller
@RequiredArgsConstructor
public class ResponseStatusController {

    private final ProductService productService;

    @GetMapping("/product/responsestatus/{id}")
    public ResponseEntity getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }
}
