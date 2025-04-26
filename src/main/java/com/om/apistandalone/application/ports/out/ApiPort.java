package com.om.apistandalone.application.ports.out;

import com.om.apistandalone.application.domain.Product;
import com.om.apistandalone.application.domain.ProductId;

import java.util.List;
import java.util.Optional;

public interface ApiPort {
    List<Product> getProducts();

    Optional<Product> getProductById(ProductId productId);
}
