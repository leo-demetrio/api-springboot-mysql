package project.base.studiesspring.repository;

import project.base.studiesspring.domain.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> listAll();
}
