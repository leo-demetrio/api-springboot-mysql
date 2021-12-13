package project.base.studiesspring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.base.studiesspring.domain.Product;
import project.base.studiesspring.requests.ProductPostRequestBody;
import project.base.studiesspring.requests.ProductPutRequestBody;
import project.base.studiesspring.service.ProductService;
import project.base.studiesspring.util.Dateutil;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {


    private final Dateutil dateutil;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> list(Pageable pageable){
        log.info(dateutil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(productService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Product>> listAll(){
//        log.info(dateutil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(productService.listAllNonPageable(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id){
        return ResponseEntity.ok(productService.findByIdOrThrowBadRequestException(id));
    }
    @GetMapping(path = "by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> findByIdAuthenticationPrincipal(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(productService.findByIdOrThrowBadRequestException(id));
    }
    @GetMapping(path = "/find")
    public ResponseEntity<List<Product>> findByName(@RequestParam String name){
        return ResponseEntity.ok(productService.findByName(name));
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> save(@RequestBody @Valid ProductPostRequestBody productPostRequestBody){
        return new ResponseEntity<>(productService.save(productPostRequestBody), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
//        log.info(dateutil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody ProductPutRequestBody productPutRequestBody){
        productService.replace(productPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
