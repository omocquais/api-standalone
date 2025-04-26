package com.om.apistandalone.adapters.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record BasketRequestDTO(@JsonProperty("productsOrdered")
                               @NotEmpty List<@Valid ProductOrderedDTO> productOrderedDTOS) {
    @Override
    public String toString() {
        return """
                Basket Request { products Ordered =%s }""".formatted(productOrderedDTOS);
    }
}
