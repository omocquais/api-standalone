package com.om.apistandalone.adapters.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.om.apistandalone.application.domain.*;

import java.math.BigDecimal;

public record ApiProductDTO(
        @JsonProperty("id")
        Integer id,

        @JsonProperty("label")
        String label,

        @JsonProperty("imported")
        boolean imported,

        @JsonProperty("type")
        ProductType type,

        @JsonProperty("price")
        BigDecimal priceExcludingTax) {

    public Product toDomain() {
        return new Product(new ProductId(id), label, imported, type, new Price(priceExcludingTax, Currency.EURO));
    }

    @Override
    public String toString() {
        return """
           Product{ id=%s, label='%s', imported=%s, type=%s, priceExcludingTax=%s }""".formatted(id, label, imported, type, priceExcludingTax);
    }
}
