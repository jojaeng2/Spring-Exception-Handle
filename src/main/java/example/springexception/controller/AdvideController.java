package example.springexception.controller;

import example.springexception.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AdvideController {

    private final ProductService productService;

    @GetMapping("/product/advice/{id}")
    public ResponseEntity getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }
}
