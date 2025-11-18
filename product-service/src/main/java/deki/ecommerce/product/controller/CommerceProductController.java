package deki.ecommerce.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class CommerceProductController {

    @GetMapping
    public String healthCheck() {
        return "Product Service is up and running!";
    }
}
