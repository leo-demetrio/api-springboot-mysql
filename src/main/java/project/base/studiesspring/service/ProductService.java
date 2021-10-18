package project.base.studiesspring.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.base.studiesspring.domain.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProductService {

    private static List<Product> products;
    static {
        products = new ArrayList<>(List.of(new Product(1L,"leo1"),new Product(2L, "leo2")));
    }

    public List<Product> listAll() {
        return products;
    }

    public Product findById(long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));
    }

    public Product save(Product product) {
        product.setId(ThreadLocalRandom.current().nextLong());
        products.add(product);
        return product;
    }

    public void delete(long id) {
        products.remove(findById(id));
    }

    public void replace(Product product) {
        delete(product.getId());
        products.add(product);
    }
}
