package com.om.apistandalone.adapters.out;

import com.om.apistandalone.adapters.out.dto.ApiProductDTO;
import com.om.apistandalone.adapters.out.dto.ApiProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClient.class);

    private final RestClient restClient;

    @Value("${api.products.url}")
    private String apiUrl;

    public ApiClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public List<ApiProductDTO> getProducts() {
        List<ApiProductDTO> apiProductDTOList = new ArrayList<>();
        try {
            LOGGER.info("Getting products from API");
            ApiProductResponse apiProductResponse = restClient.get()
                    .uri(URI.create(apiUrl))
                    .retrieve().body(ApiProductResponse.class);
            if (apiProductResponse != null) {
                apiProductDTOList = apiProductResponse.getProducts();
                LOGGER.info("Found {} products: {}", apiProductDTOList.size(), apiProductDTOList);
            }
        } catch (Exception e) {
            LOGGER.error("Error when retrieving the product list from API: {}", e.getMessage());
        }
        return apiProductDTOList;

    }
}
