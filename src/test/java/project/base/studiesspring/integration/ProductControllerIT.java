package project.base.studiesspring.integration;


import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import project.base.studiesspring.domain.Product;
import project.base.studiesspring.repository.ProductRepository;
import project.base.studiesspring.requests.ProductPostRequestBody;
import project.base.studiesspring.requests.ProductPutRequestBody;
import project.base.studiesspring.util.ProductCreator;
import project.base.studiesspring.util.ProductPostRequestBodyCreator;
import project.base.studiesspring.util.ProductPutRequestBodyCreator;
import project.base.studiesspring.wrapper.PageableResponse;

import java.util.Collections;
import java.util.List;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Return list products inside page object when successful")
    void list_ReturnListOfProductsInsidePageObject_WhenSuccessful(){
        Product productSaved = productRepository.save(ProductCreator.createProductForBeSaved());
        log.info(productSaved);
        String expectedName = productSaved.getName();
        PageableResponse<Product> productPage = restTemplate.exchange("/products", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Product>>() {
                }).getBody();

        Assertions.assertThat(productPage).isNotNull();
        Assertions.assertThat(productPage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(productPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Return list products when successful")
    void listAll_ReturnListOfProducts_WhenSuccessful(){
        Product productSaved = productRepository.save(ProductCreator.createProductForBeSaved());
        String expectedName = productSaved.getName();
        log.info(restTemplate.exchange("/products/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {
                }).getBody());
        List<Product> products = restTemplate.exchange("/products/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {
                }).getBody();

        Assertions.assertThat(products)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(products.get(0).getName()).isEqualTo(expectedName);
    }
    @Test
    @DisplayName("Return product when successful")
    void findById_ReturnProduct_WhenSuccessful(){
        Product productSaved = productRepository.save( Product.builder().name("Leo test 01").id(1L).build());
        Long productId = productSaved.getId();

        Product productResult = restTemplate.getForObject("/products/{id}",Product.class, productId);
        Assertions.assertThat(productResult).isNotNull();

        Assertions.assertThat(productResult.getId()).isNotNull().isEqualTo(productId);
    }
    @Test
    @DisplayName("Return list of product when successful")
    void findByName_ReturnListOfProduct_WhenSuccessful(){
        Product productSaved = productRepository.save(ProductCreator.createProductForBeSaved());

        String expectedName = productSaved.getName();
        String url = String.format("/products/find?name=%s",expectedName);
        List<Product> productList = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {
                }).getBody();

        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList)
                .isNotEmpty()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(productList.get(0).getName()).isEqualTo(expectedName);
    }
    @Test
    @DisplayName("Return empty list when not found")
    void findByName_ReturnEmptyList_WhenProductNotFound(){
        List<Product> productList = restTemplate.exchange("/products/find?name=anyName", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {
                }).getBody();

        Assertions.assertThat(productList)
                .isNotNull()
                .isNotEmpty();

    }

    @Test
    @DisplayName("Return product")
    void save_ReturnProduct_WhenSuccessful(){
       ProductPostRequestBody productPostRequestBody = ProductPostRequestBodyCreator.createProductPostRequestBody();
       ResponseEntity<Product> productResponseEntity = restTemplate.postForEntity("/products",productPostRequestBody,Product.class);
       Assertions.assertThat(productResponseEntity).isNotNull();
       Assertions.assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
       Assertions.assertThat(productResponseEntity.getBody()).isNotNull();
       Assertions.assertThat(productResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace update product when successful")
    void replace_UpdateProduct_WhenSuccessful(){
        Product productSaved = productRepository.save(ProductCreator.createProductForBeSaved());
        productSaved.setName("new name");
        ResponseEntity<Void> productResponseEntity = restTemplate.exchange("/products",HttpMethod.PUT,new HttpEntity<>(productSaved),Void.class);
        log.info(productResponseEntity);
        Assertions.assertThat(productResponseEntity).isNotNull();
        Assertions.assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("delete remove product when successful")
    void delete_RemoveProduct_WhenSuccessful(){
        Product productSaved = productRepository.save(ProductCreator.createProductForBeSaved());
        ResponseEntity<Void> productResponseEntity = restTemplate.exchange("/products/{id}",HttpMethod.DELETE,null,Void.class, productSaved.getId());
        log.info(productResponseEntity);
        Assertions.assertThat(productResponseEntity).isNotNull();
        Assertions.assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
