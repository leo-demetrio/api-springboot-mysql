package project.base.studiesspring.client;


import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import project.base.studiesspring.domain.Product;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Product> productResponseEntity = new RestTemplate().getForEntity("http://localhost:8080/api/v1/products/7", Product.class);
        log.info(productResponseEntity);

        Product product = new RestTemplate().getForObject("http://localhost:8080/api/v1/products/7", Product.class);
        log.info(product);

        Product[] productArray = new RestTemplate().getForObject("http://localhost:8080/api/v1/products/all", Product[].class);
        log.info(Arrays.toString(productArray));

        ResponseEntity<List<Product>> productList =
                new RestTemplate().exchange("http://localhost:8080/api/v1/products/all", HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});
        log.info(productList.getBody());

    }
}
