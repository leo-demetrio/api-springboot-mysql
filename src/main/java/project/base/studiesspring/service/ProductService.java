package project.base.studiesspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.base.studiesspring.domain.Product;
import project.base.studiesspring.repository.ProductRepository;
import project.base.studiesspring.requests.ProductPostRequestBody;
import project.base.studiesspring.requests.ProductPutRequestBody;


import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> listAll() {

        return productRepository.findAll();
    }

    public Product findByIdOrThrowBadRequestException(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));
    }

    public Product save(ProductPostRequestBody productPostRequestBody) {
        Product product = Product.builder().name(productPostRequestBody.getName()).build();
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(ProductPutRequestBody productPutRequestBody) {
        findByIdOrThrowBadRequestException(productPutRequestBody.getId());
        Product product = Product.builder()
                .id(productPutRequestBody.getId())
                .name(productPutRequestBody.getName())
                .build();
        productRepository.save(product);
    }
}
