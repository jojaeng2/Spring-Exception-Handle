package example.springexception.service;

import example.springexception.exception.NoSuchElementFoundException;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public ResponseEntity getProduct(String id) {
        if(id.equals("123")) {
            throw new NoSuchElementFoundException();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
