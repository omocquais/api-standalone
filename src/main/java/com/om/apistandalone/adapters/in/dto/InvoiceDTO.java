package com.om.apistandalone.adapters.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record InvoiceDTO(@JsonProperty("uuid") UUID uuid,
                         @JsonProperty("products") List<ProductDTO> products,
                         @JsonProperty("basket") BasketDTO basket) {
    @Override
    public String toString() {
        return """
                Invoice { uuid=%s, basket=%s }
                """.formatted(uuid, basket);
    }
}
