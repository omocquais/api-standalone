package com.om.apistandalone.adapters.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductOrderedDTO(@JsonProperty("product") @NotNull ProductDTO productDTO,
                                @JsonProperty("quantity") @Min(value = 1) Integer quantity) {

    @Override
    public String toString() {
        return """
                Product Ordered { product=%s, quantity=%d }""".formatted(productDTO, quantity);
    }

}
