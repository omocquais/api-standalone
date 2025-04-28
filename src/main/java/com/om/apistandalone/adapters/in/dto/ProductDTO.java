package com.om.apistandalone.adapters.in.dto;

import com.om.apistandalone.application.domain.ProductType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductDTO(@NotNull Integer id,
                         @NotNull String label,
                         boolean imported,
                         @NotNull ProductType type,
                         @NotNull @PositiveOrZero BigDecimal priceExcludingTax,
                         @NotNull @PositiveOrZero BigDecimal priceIncludingTax) {
    @Override
    public String toString() {
        return """
            Product { id=%d, label='%s', imported=%b, type=%s, priceExcludingTax=%.2f, priceIncludingTax=%.2f }
            """.formatted(id, label, imported, type, priceExcludingTax, priceIncludingTax);
    }
}
