package com.om.apistandalone.adapters.out.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiProductResponse {

    @JsonProperty("products")
    public List<ApiProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ApiProductDTO> products) {
        this.products = products;
    }

    private List<ApiProductDTO> products = new ArrayList<>();


}
