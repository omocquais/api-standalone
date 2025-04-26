package com.om.apistandalone.adapters.out;

import com.om.apistandalone.adapters.out.dto.ApiProductDTO;
import com.om.apistandalone.application.domain.Product;
import com.om.apistandalone.application.domain.ProductId;
import com.om.apistandalone.application.ports.out.ApiPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiAdapter implements ApiPort {

    private final ApiClient apiClient;

    public ApiAdapter(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public List<Product> getProducts() {
        return apiClient.getProducts().stream().map(ApiProductDTO::toDomain).toList();
    }

    @Override
    public Optional<Product> getProductById(ProductId productId) {
        return apiClient.getProducts().stream().filter(apiProductDTO -> apiProductDTO.id().equals(productId.id())).map(ApiProductDTO::toDomain).findFirst();
    }
}
