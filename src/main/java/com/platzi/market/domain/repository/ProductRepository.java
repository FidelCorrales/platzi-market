package com.platzi.market.domain.repository;

import com.platzi.market.domain.Product;

import java.util.List;
import java.util.Optional;
/*
 * Reglas que va a tener nuestro dominio al momento que cualquier
 * repositorio quiera acceder a productos de una BD.
 * Permite no acoplar nuestra solución a una base de datos especifica
 */

public interface ProductRepository {

    List<Product> getAll();
    Optional<List<Product>> getByCategory(int categoryId);
    Optional<List<Product>> getScarseProducts(int quantity);
    Optional<Product> getProduct(int productId);
    Product save(Product product);
    void delete(int productId);

}
